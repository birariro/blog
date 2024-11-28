const {Client} = require("@notionhq/client");
const {NotionToMarkdown} = require("notion-to-md");
const {S3Client, PutObjectCommand} = require('@aws-sdk/client-s3');
const {v4: uuidv4} = require('uuid'); // Unique ID 생성용
const {DynamoDBClient} = require("@aws-sdk/client-dynamodb");
const {DynamoDBDocumentClient, GetCommand, PutCommand} = require("@aws-sdk/lib-dynamodb");
const https = require('https');
const dotenv = require("dotenv");
dotenv.config();
const client = process.env.IS_OFFLINE === 'true'
    ? new DynamoDBClient({endpoint: process.env.DYNAMODB_LOCAL_ENDPOINT})
    : new DynamoDBClient({});
const dynamo = DynamoDBDocumentClient.from(client);

const s3Client = new S3Client({
    region: process.env.REGION
});
const bucketName = process.env.RESOURCE_BUCKET_NAME;
const tableName = process.env.DYNAMO_TABLE_NAME;
const NOTION_API_KEY = process.env.NOTION_API_KEY;
const NOTION_DATABASE_ID = process.env.NOTION_DATABASE_ID;

const notionClient = new Client({auth: NOTION_API_KEY});
const n2m = new NotionToMarkdown({notionClient: notionClient});
const notionDatabaseId = NOTION_DATABASE_ID;

async function getArticleFromDynamoDatabase(id) {
    try {
        const result = await dynamo.send(
            new GetCommand({
                TableName: tableName,
                Key: {
                    id: id,
                },
            })
        );
        console.debug("DynamoDB result for id:", id, result);
        return result;
    } catch (error) {
        console.error("Error fetching article from DynamoDB:", error);
        throw new Error("Could not fetch article from DynamoDB");
    }
}

async function saveArticleFromDynamoDatabase(article) {
    await dynamo.send(
        new PutCommand({
            TableName: tableName,
            Item: {
                id: article.id,
                tags: article.tags,
                title: article.title,
                summary: article.summary,
                content: article.content,
                thumbnail: article.thumbnail,
                createdAt: article.createdAt,
                updatedAt: article.updatedAt,
            },
        })
    );
}

async function getArticleFromNotionDatabase() {
    const response = await notionClient.databases.query({
        database_id: notionDatabaseId,
        filter: {
            property: "status",
            status: {
                equals: "true",
            },
        },
        sorts: [
            {
                property: "releaseAt",
                direction: "descending",
            },
        ],
    });
    return response.results;
}

async function syncArticles() {
    console.debug("Start Sync Articles")
    const articles = await getArticleFromNotionDatabase();

    console.debug("NotionDatabase Article Count :" + articles.length)
    const modifyTitles = [];
    for (const article of articles) {
        console.debug("article :" + JSON.stringify(article))

        const id = article.id;
        const tags = article.properties.tags.multi_select.map(item => item.name);
        const createdAt = article.properties.releaseAt.rich_text[0].plain_text;
        const updatedAt = article.last_edited_time;
        const title = article.properties.article.title[0].plain_text;
        
        const persistArticle = await getArticleFromDynamoDatabase(id);
        if (newArticle(persistArticle) || modifyArticle(updatedAt, persistArticle)) {

            console.debug("Persistence Article : " + title);

            let persistImages = await replaceNotionImageToPersistImage(id);
            const thumbnail = await findThumbnailIfNecessaryReplace(article, persistImages);
            const content = await notionPageToMarkdown(id);
            const summary = extractSummary(content);
            await saveArticleFromDynamoDatabase({
                id: id,
                tags: tags,
                createdAt: createdAt,
                updatedAt: updatedAt,
                title: title,
                thumbnail: thumbnail,
                summary: summary,
                content: content,
            });
            modifyTitles.push(title);
        } else {
            console.debug("Non Persistence Article : " + title);
        }
    }

    return {
        modify: modifyTitles
    };
}


/**
 * 썸네일을 가지고 온다 만약 존재하지 않는다면 컨텐츠 내의 이미지중 첫번째를 선택하며 해당 이미지를 썸네일로 등록한다.
 * @param thumbnail
 * @param imageUrls
 * @returns {Promise<void>}
 */
const findThumbnailIfNecessaryReplace = async (article, imageUrls) => {
    const thumbnail = article?.properties?.thumbnail?.files?.[0]?.external?.url;
    if (thumbnail !== undefined) {
        return thumbnail;
    }
    if (imageUrls.length >= 1) {
        let imageUrl = imageUrls[0];

        try {
            await notionClient.pages.update({
                page_id: article.id,
                properties: {
                    thumbnail: {
                        files: [
                            {
                                name: imageUrl,
                                type: 'external',
                                external: {
                                    url: imageUrl,
                                },
                            },
                        ],
                    },
                },
            });
            console.log("successfully modify thumbnail url : " + imageUrl)
        } catch (error) {
            console.log("Error modify thumbnail url : " + imageUrl, error)
        }

    }


}
/**
 * 노션의 이미지 URL 을 영구적인 URL로 교체한다
 * @param pageId
 * @returns {Promise<*[]>}
 */
const replaceNotionImageToPersistImage = async (pageId) => {
    const persistImage = [];
    const notionBlocks = await findNotionImageBlocks(pageId);
    console.log("replace image running size : " + notionBlocks.length)
    for (const notionBlock of notionBlocks) {
        let base64Image = await downloadImageToBase64(notionBlock.image.file.url);
        let permanentImageUrl = await uploadImageToS3(base64Image);
        persistImage.push(permanentImageUrl)
        await notionClient.blocks.update({
            block_id: notionBlock.id,
            image: {
                external: {
                    url: permanentImageUrl,
                },
            },
        })
    }

    return persistImage;
}

async function uploadImageToS3(base64ImagePromise) {
    try {
        const base64Image = await base64ImagePromise;
        const buffer = Buffer.from(base64Image, 'base64');

        // 고유 파일 이름 생성
        const fileName = `images/${uuidv4()}.jpg`;

        // S3 업로드 요청 생성
        const uploadParams = {
            Bucket: bucketName,
            Key: fileName,
            Body: buffer,
            ContentType: 'image/jpeg', // 이미지 MIME 타입
        };

        // S3에 업로드
        const command = new PutObjectCommand(uploadParams);
        await s3Client.send(command);

        // 업로드된 파일의 URL 생성
        const s3Url = `${process.env.RESOURCE_API}/${fileName}`;
        return s3Url;
    } catch (error) {
        console.error('Error uploading to S3:', error);
        throw error;
    }
}

function downloadImageToBase64(url) {
    return new Promise((resolve, reject) => {
        const req = https.request(url, (response) => {
            const chunks = [];

            response.on('data', function (chunk) {
                chunks.push(chunk);
            });

            response.on('end', function () {
                const result = Buffer.concat(chunks);
                resolve(result.toString('base64'));
            });
        });
        req.on('error', reject);
        req.end();
    });
}


/**
 * 이미지 블록중 노션의 url을 사용중인 이미지 블록을 얻는다
 * @param pageId
 * @returns {Promise<*[]>}
 */
const findNotionImageBlocks = async (pageId) => {
    const allBlocks = await findImageBlocksInPage(pageId)

    return allBlocks.filter(
        block => 'image' in block && block.image.type === 'file',
    )
}

/**
 * 블록중 이미지 블록을 얻는다
 * @param pageId
 * @returns {Promise<*[]>}
 */
const findImageBlocksInPage = async (pageId) => {
    const allBlocks = await findPageInBlocks(pageId)
    return allBlocks.filter(
        block => 'type' in block && block.type === 'image',
    )
}

/**
 * 페이지의 모든 블록을 얻는다
 * @param blockOrPageId
 * @returns {Promise<*[]>}
 */
const findPageInBlocks = async (blockOrPageId) => {
    let hasMore = true
    let nextCursor = null
    const blocks = []

    // 요청당 불러올 수 있는 블락 응답의 크기가 한정되어 있어 모든 블록들을 불러올때 까지
    // notion 페이지의 block들을 불러오는 과정들을 반복해야 한다
    while (hasMore) {
        const result = await notionClient.blocks.children.list({
            block_id: blockOrPageId,
            start_cursor: nextCursor ?? undefined,
        })

        blocks.push(...result.results)
        hasMore = result.has_more
        nextCursor = result.next_cursor

        if (hasMore) {
            console.log('load more blocks in page...')
        }
    }

    // 블록들 중 자식요소로 있는 블록들을 찾아 이 역시 블러온다
    // 예를 들면 toggle block이 있을 것이다.
    const childBlocks = await Promise.all(
        blocks
            .filter(block => 'has_children' in block && block.has_children)
            .map(async block => {
                const childBlocks = await findPageInBlocks(block.id)
                return childBlocks
            }),
    )

    return [...blocks, ...childBlocks.flat()]
}

/**
 * 빈 공백이 무시되는 상황이 발생하기에 직접 개행을 추가
 * @param id
 * @returns {Promise<string>}
 */
async function notionPageToMarkdown(id) {
    const mdblocks = await n2m.pageToMarkdown(id);
    const mdString = mdblocks.map(block => {
        let markdownText = n2m.toMarkdownString([block]).parent;

        if (markdownText === undefined) {
            return "\n"
        }

        //해더 테그일경우 개행 제거
        if (markdownText.startsWith("\n")) {
            markdownText = markdownText.slice(1);
        }
        if (markdownText.endsWith("\n\n")) {
            markdownText = markdownText.slice(0, -1);
        }
        return markdownText

    }).join('');
    return mdString;
}

function newArticle(persistArticle) {
    return persistArticle === undefined || persistArticle.Item === undefined;
}

function modifyArticle(updatedAt, persistArticle) {
    return persistArticle.Item.updatedAt !== updatedAt;
}

function extractSummary(content) {
    const center = content.length / 2;
    const start = content.slice(0, 50);
    const end = content.slice(center, center + 50);

    return `${start}...${end}`;
}

module.exports.handler = async (event) => {
    let body;
    let statusCode = 200;
    const headers = {
        "Content-Type": "application/json",
    };

    try {
        body = await syncArticles();
    } catch (err) {
        statusCode = 400;
        body = err.message;
    } finally {
        body = JSON.stringify(body);
    }

    return {
        statusCode,
        body,
        headers,
    };
};
