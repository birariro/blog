const {DynamoDBClient} = require("@aws-sdk/client-dynamodb");
const {DynamoDBDocumentClient, GetCommand, ScanCommand} = require("@aws-sdk/lib-dynamodb");

const client = process.env.IS_OFFLINE === 'true'
    ? new DynamoDBClient({endpoint: process.env.DYNAMODB_LOCAL_ENDPOINT})
    : new DynamoDBClient({});

const dynamo = DynamoDBDocumentClient.from(client);
const tableName = process.env.DYNAMO_TABLE_NAME;
const origin = process.env.ORIGIN_URL;

module.exports.handler = async (event) => {
    let body;
    let statusCode = 200;
    const headers = {
        'Access-Control-Allow-Origin': origin,
        'Access-Control-Allow-Credentials': true,
        "Content-Type": "application/json",
    };

    try {
        if (event.httpMethod === "GET") {
            if (event.path === "/article") {
                body = await dynamo.send(
                    new ScanCommand({
                        TableName: tableName,
                        ProjectionExpression: "id, title, summary,thumbnail, createdAt",
                    })
                );

                // createdAt 기준으로 내림차순 정렬
                body.Items.sort((a, b) => {
                    return new Date(b.createdAt) - new Date(a.createdAt);
                });

                body = body.Items.map(item => {
                    return {
                        id: item.id,
                        title: item.title,
                        thumbnail: item.thumbnail,
                        summary: item.summary,
                        createdAt: item.createdAt,
                    };
                });
            } else if (event.path.startsWith("/article/")) { // /article/{id} 형태 처리
                const id = event.path.split("/")[2]; // URL에서 ID 추출
                body = await dynamo.send(
                    new GetCommand({
                        TableName: tableName,
                        Key: {
                            id: id,
                        },
                    })
                );

                if (!body.Item) {
                    throw new Error(`No item found for id: ${id}`);
                }

                body = {
                    id: body.Item.id,
                    tags: body.Item.tags,
                    title: body.Item.title,
                    content: body.Item.content,
                    createdAt: body.Item.createdAt,
                    updatedAt: body.Item.updatedAt,
                };
            } else {
                throw new Error(`Unsupported route: "${event.path}"`);
            }
        } else {
            throw new Error(`Unsupported method: "${event.httpMethod}"`);
        }
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
