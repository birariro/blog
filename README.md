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

#### github action secrets

- DOCKER_HUB_ID : docker hub username
- DOCKER_HUB_REPOSITORY : docker hub repository name
- DOCKER_HUB_TOKEN : [docker hub Personal access tokens](https://docs.docker.com/security/for-developers/access-tokens/)

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


