const {DynamoDBClient} = require("@aws-sdk/client-dynamodb");
const {DynamoDBDocumentClient, GetCommand, ScanCommand} = require("@aws-sdk/lib-dynamodb");

const client = new DynamoDBClient({});
const dynamo = DynamoDBDocumentClient.from(client);
const tableName = process.env.DYNAMO_TABLE_NAME;

module.exports.handler = async (event) => {
    let body;
    let statusCode = 200;
    const headers = {
        "Content-Type": "application/json",
    };

    try {
        if (event.httpMethod === "GET") {
            switch (event.path) {

                case "/article":
                    body = await dynamo.send(
                        new ScanCommand({
                            TableName: tableName,
                            ProjectionExpression: "id, title, createdAt",
                        })
                    );

                    body.Items.sort((a, b) => {
                        return new Date(b.createdAt) - new Date(a.createdAt); // 내림차순 정렬
                    });
                    
                    body = body.Items.map(item => {
                        return {
                            id: item.id,
                            title: item.title,
                            createdAt: item.createdAt, // 필요 시 createdAt 포함
                        };
                    });
                    break;

                case "/article/{id}":
                    const id = event.pathParameters.id;
                    body = await dynamo.send(
                        new GetCommand({
                            TableName: tableName,
                            Key: {
                                id: id,
                            },
                        })
                    );

                    body = {
                        id: body.Item.id,
                        tags: body.Item.tags,
                        title: body.Item.title,
                        content: body.Item.content,
                        createdAt: body.Item.createdAt,
                        updatedAt: body.Item.updatedAt,
                    };
                    break;

                default:
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
