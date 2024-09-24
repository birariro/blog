const {Client} = require("@notionhq/client");
const {NotionToMarkdown} = require("notion-to-md");
const {DynamoDBClient} = require("@aws-sdk/client-dynamodb");
const {DynamoDBDocumentClient, GetCommand, PutCommand} = require("@aws-sdk/lib-dynamodb");

const dotenv = require("dotenv");
dotenv.config();
const client = new DynamoDBClient({});
const dynamo = DynamoDBDocumentClient.from(client);


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
                content: article.content,
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
        const id = article.id;
        const tags = article.properties.tags.multi_select.map(item => item.name);
        const createdAt = article.properties.releaseAt.rich_text[0].plain_text;
        const updatedAt = article.last_edited_time;
        const title = article.properties.article.title[0].plain_text;
        const persistArticle = await getArticleFromDynamoDatabase(id);

        if (newArticle(persistArticle) || modifyArticle(updatedAt, persistArticle)) {

            console.debug("Persistence Article : " + title);
            const content = await notionPageToMarkdown(id);
            await saveArticleFromDynamoDatabase({
                id: id,
                tags: tags,
                createdAt: createdAt,
                updatedAt: updatedAt,
                title: title,
                content: content,
            });
            modifyTitles.push(title);
        }else{
            console.debug("Non Persistence Article : " + title);
        }
    }

    return {
        modify: modifyTitles
    };
}

async function notionPageToMarkdown(id) {
    const mdblocks = await n2m.pageToMarkdown(id);
    const mdString = n2m.toMarkdownString(mdblocks);
    return mdString.parent;
}

function newArticle(persistArticle) {
    return persistArticle === undefined || persistArticle.Item === undefined;
}

function modifyArticle(updatedAt, persistArticle) {
    return persistArticle.Item.updatedAt !== updatedAt;
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
