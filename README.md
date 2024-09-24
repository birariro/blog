<p align='center'>
  <img src="https://github.com/birariro/blog/blob/main/arch.png"/>
</p>

### web demo

```shell
cd frontend && npm start   
```

### use

#### github action secrets setting

- AWS_ACCESS_KEY_ID :  aws iam access key
- AWS_SECRET_ACCESS_KEY : aws iam secret key
- AWS_CLOUDFRONT_ID : web cloud front id
- AWS_S3_BUCKET_NAME : web s3 bucket name
- AWS_SYNC_API_GATEWAY_URL : ```sync_notion_to_dynamo``` api gateway url

#### Get Secret Value

- [Notion Api Key](https://www.notion.so/profile/integrations)
- [Notion Database ID](https://developers.notion.com/reference/retrieve-a-database)

#### Registration Secret Value from SSM Parameter Store

[AWS Systems Manager Parameter Store](https://docs.aws.amazon.com/ko_kr/systems-manager/latest/userguide/systems-manager-parameter-store.html)

- Open [AWS Systems Manager](https://ap-northeast-2.console.aws.amazon.com/systems-manager/home?region=ap-northeast-2) page
- Move Parameter Store
- Registration SecureString
    - /notion/api_key : Notion Api Key
    - /notion/database_id : Notion Database ID

#### deploy serverless

```
serverless deploy
```

#### frontend .env.production api path setting

```.env.production
REACT_APP_API_BASE_URL: {'get_article_from_dynamo' api gateway path}
```
