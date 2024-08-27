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
                property: "Created time",
                direction: "descending",
            },
        ],
    });
    return response.results;
}


async function syncArticles() {

    const articles = await getArticleFromNotionDatabase()

    var entitys = []
    for (const article of articles) {
        const id = article.id;
        const createdAt = article.created_time;
        const updatedAt = article.last_edited_time;
        const title = article.properties.article.title[0].plain_text;

        const mdblocks = await n2m.pageToMarkdown(article.id);
        const mdString = n2m.toMarkdownString(mdblocks);
        const content = mdString.parent;

        const entity = {
            id: id,
            createdAt: createdAt,
            updatedAt: updatedAt,
            title: title,
            content: content,
        };
        entitys.push(entity);

    }

    const newArticleTitles = [];
    const modifyArticleTitles = [];
    for (const entity of entitys) {

        console.log("search: " + entity.title)

        const persistArticle = await getArticleFromDynamoDatabase(entity.id)
        if (newArticle(persistArticle)) {
            newArticleTitles.push(entity.title);
            await saveArticleFromDynamoDatabase(entity);
        } else if (modifyArticle(entity, persistArticle)) {
            modifyArticleTitles.push(entity.title);
            await saveArticleFromDynamoDatabase(entity);
        }
    }
    return {
        new: newArticleTitles,
        modify: modifyArticleTitles
    };

}

function newArticle(persistArticle) {
    return persistArticle === undefined || persistArticle.Item === undefined
}

function modifyArticle(entity, persistArticle) {
    return persistArticle.Item.updatedAt != entity.updatedAt
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

