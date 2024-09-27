<p align='center'>
  <img src="https://github.com/birariro/blog/blob/main/arch.png"/>
</p>

## use

### web demo

```shell
cd frontend && \
npm start   
```

### pre work

#### Get Secret Value

- [Notion Api Key](https://www.notion.so/profile/integrations)
- [Notion Database ID](https://developers.notion.com/reference/retrieve-a-database)

### deploy

#### Registration Secret Value from SSM Parameter Store

[AWS Systems Manager Parameter Store](https://docs.aws.amazon.com/ko_kr/systems-manager/latest/userguide/systems-manager-parameter-store.html)

- Open [AWS Systems Manager](https://ap-northeast-2.console.aws.amazon.com/systems-manager/home?region=ap-northeast-2) page
- Move Parameter Store
- Registration SecureString
    - /notion/api_key : Notion Api Key
    - /notion/database_id : Notion Database ID

or

```shell
aws ssm put-parameter \
    --name "/notion/api_key" \
    --value "{Notion Api Key}" \
    --type "SecureString" \
&& \    
aws ssm put-parameter \
    --name "/notion/database_id" \
    --value "{Notion Database ID}" \
    --type "SecureString"  
```

#### deploy serverless

```shell
cd serverless && \
serverless deploy
```

#### frontend path setting

./frontend/.env.production

```
REACT_APP_API_BASE_URL: {'get_article_from_dynamo' api path}
```

#### github action secrets setting

- AWS_ACCESS_KEY_ID :  aws iam access key
- AWS_SECRET_ACCESS_KEY : aws iam secret key
- AWS_CLOUDFRONT_ID : web cloud front id
- AWS_S3_BUCKET_NAME : web s3 bucket name
- AWS_SYNC_API_GATEWAY_URL : ```sync_notion_to_dynamo``` api gateway url

### local build

#### create serverless '.env' file

./serverless/.env

```
NOTION_API_KEY={Notion Api Key}
NOTION_DATABASE_ID={Notion Database ID}
```

#### aws configure setting

```shell
aws configure
```

#### run local dynamodb

```shell
cd serverless && \
docker-compose up -d && \
aws dynamodb create-table \
    --table-name blog_article_table \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --endpoint-url http://localhost:8000
```

#### run local serverless

```shell
cd serverless && \
serverless offline
```

#### local web api path setting

./frontend/.env

```
REACT_APP_API_BASE_URL: {'get_article_from_dynamo' api path}
```

#### run local web

```shell
cd frontend && \
npm start   
```
