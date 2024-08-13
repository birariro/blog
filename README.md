## demo run

``` 
docker compose -f compose.yml up --build --remove-orphans -d
```

```
./gradlew bootrun -P profile=demo   
cd frontend && npm start
```

## deploy

### backend

#### application-secret

```
aws:
    key:    
        access: {s3 bucket access key}
        secret: {s3 bucket private key}

```

#### github action secrets

- DOCKER_HUB_ID : docker hub username
- DOCKER_HUB_REPOSITORY : docker hub repository name
- DOCKER_HUB_TOKEN : [docker hub Personal access tokens](https://docs.docker.com/security/for-developers/access-tokens/)
- AWS_ACCESS_KEY_ID :  s3 access key
- AWS_SECRET_ACCESS_KEY : s3 secret key
- AWS_CLOUDFRONT_ID : web cloud front id
- AWS_S3_BUCKET_NAME : web s3 bucket name

### frontend

#### api path

```.env.production
REACT_APP_API_BASE_URL: {api path}
```

#### build

```sh
cd frontend && npm run build
```

[jasypt en/decoding](https://www.devglan.com/online-tools/jasypt-online-encryption-decryption)


