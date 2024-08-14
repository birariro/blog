#!/bin/bash

DOCKER_HUB_ID=""
DOCKER_HUB_REPOSITORY=""
JASYPT_PASSWORD=""

docker stop ${DOCKER_HUB_REPOSITORY}
docker rm ${DOCKER_HUB_REPOSITORY}
docker rmi ${DOCKER_HUB_ID}/${DOCKER_HUB_REPOSITORY}
docker pull ${DOCKER_HUB_ID}/${DOCKER_HUB_REPOSITORY}
docker run -d -p 80:3844 -v ./logs:/logs -e jasypt=${JASYPT_PASSWORD} --network workspace_blog --name ${DOCKER_HUB_REPOSITORY} ${DOCKER_HUB_ID}/${DOCKER_HUB_REPOSITORY}