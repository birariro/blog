<p align='center'>
  <img src="https://github.com/birariro/blog/blob/main/image/arch.png"/>
</p>

### web demo

```shell
cd frontend && npm start   
```

### use

#### create dynamo table

create aws dynamoDB in table. <br>
ex: blog_article_table

#### lambda upload and API Gateway connect

- ```sync_notion_to_dynamo``` : Import article from the notion database and save them to aws dynamoDB
- ```get_article_form_dynamo``` : Find article from aws dynamoDB

#### lambda env

- NOTION_API_KEY: [api key issued](https://www.notion.so/profile/integrations)
- NOTION_DATABASE_ID:[Notion database ID](https://developers.notion.com/reference/retrieve-a-database)
- DYNAMO_TABLE_NAME= aws dynamo table name(ex: blog_article_table)

#### api gateway CORS setting

- ```sync_notion_to_dynamo``` api gateway : github repository url
- ```get_article_form_dynamo``` api gateway : web url

#### frontend .env.production api path setting

```.env.production
REACT_APP_API_BASE_URL: {'get_article_form_dynamo' api gateway path}
```

#### github action secrets setting

- AWS_ACCESS_KEY_ID :  aws iam access key
- AWS_SECRET_ACCESS_KEY : aws iam secret key
- AWS_CLOUDFRONT_ID : web cloud front id
- AWS_S3_BUCKET_NAME : web s3 bucket name
- AWS_SYNC_API_GATEWAY_URL : ```sync_notion_to_dynamo``` api gateway url

