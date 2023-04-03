#!/usr/bin/env bash

PREFIX=cnj
NAME=docker
IMAGE_NAME=$PREFIX/$NAME

mvn -DskipTests=true clean package
docker build -t $IMAGE_NAME .
docker run -p 8082:8082 -e SERVER_PORT=8082 $IMAGE_NAME