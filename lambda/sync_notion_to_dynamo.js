import {Client} from "@notionhq/client";
import {NotionToMarkdown} from "notion-to-md";
import dotenv from "dotenv";
import {DynamoDBClient} from "@aws-sdk/client-dynamodb";
import {DynamoDBDocumentClient, GetCommand, PutCommand,} from "@aws-sdk/lib-dynamodb";

dotenv.config();

const client = new DynamoDBClient({});
const dynamo = DynamoDBDocumentClient.from(client);

const tableName = process.env.DYNAMO_TABLE_NAME
const NOTION_API_KEY = process.env.NOTION_API_KEY
const NOTION_DATABASE_ID = process.env.NOTION_DATABASE_ID

const notionClient = new Client({auth: NOTION_API_KEY});
const n2m = new NotionToMarkdown({notionClient: notionClient});
const notionDatabaseId = NOTION_DATABASE_ID;

async function getArticleFromDynamoDatabase(id) {
    return await dynamo.send(
        new GetCommand({
            TableName: tableName,
            Key: {
                id: id,
            },
        })
    );
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

    const articles = await getArticleFromNotionDatabase()

    const modifyTitles = [];
    for (const article of articles) {

        console.log("article: " + JSON.stringify(article))

        const id = article.id;
        const tags = article.properties.tags.multi_select.map(item => item.name);
        const createdAt = article.properties.releaseAt.rich_text[0].plain_text;
        const updatedAt = article.last_edited_time;
        const title = article.properties.article.title[0].plain_text;

        const persistArticle = await getArticleFromDynamoDatabase(id);

        if (newArticle(persistArticle) || modifyArticle(updatedAt, persistArticle)) {

            console.log(`modify article: ${title}`)
            const content = await notionPageToMarkdown(id);
            await saveArticleFromDynamoDatabase(
                {
                    id: id,
                    tags: tags,
                    createdAt: createdAt,
                    updatedAt: updatedAt,
                    title: title,
                    content: content,
                }
            );
            modifyTitles.push(title);
        }
    }


    return {
        modify: modifyTitles
    };

}


async function notionPageToMarkdown(id) {
    const mdblocks = await n2m.pageToMarkdown(id);
    const mdString = n2m.toMarkdownString(mdblocks);
    const content = mdString.parent;
    return content;
}

function newArticle(persistArticle) {
    return persistArticle === undefined || persistArticle.Item === undefined
}

function modifyArticle(updatedAt, persistArticle) {
    return persistArticle.Item.updatedAt !== updatedAt
}

export const handler = async (event, context) => {
    let body;
    let statusCode = 200;
    const headers = {
        "Content-Type": "application/json",
    };

    try {
        body = await syncArticles()
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

