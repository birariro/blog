import {DynamoDBClient} from "@aws-sdk/client-dynamodb";
import {DynamoDBDocumentClient, GetCommand, ScanCommand,} from "@aws-sdk/lib-dynamodb";


const client = new DynamoDBClient({});
const dynamo = DynamoDBDocumentClient.from(client);
const tableName = process.env.DYNAMO_TABLE_NAME

export const handler = async (event, context) => {
    let body;
    let statusCode = 200;
    const headers = {
        "Content-Type": "application/json",
    };

    try {
        switch (event.routeKey) {

            case "GET /article/{id}":
                body = await dynamo.send(
                    new GetCommand({
                        TableName: tableName,
                        Key: {
                            id: event.pathParameters.id,
                        },
                    })
                );

                body = {
                    id: body.Item.id,
                    title: body.Item.title,
                    content: body.Item.content,
                    createdAt: body.Item.createdAt,
                    updatedAt: body.Item.updatedAt,
                }
                break;
            case "GET /article":
                body = await dynamo.send(
                    new ScanCommand({TableName: tableName, ProjectionExpression: "id, title",})
                );

                body = body.Items.map(item => {
                    return {
                        id: item.id,
                        title: item.title
                    };
                });
                break;

            default:
                throw new Error(`Unsupported route: "${event.routeKey}"`);
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
