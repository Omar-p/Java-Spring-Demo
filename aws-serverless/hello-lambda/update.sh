#!/usr/bin/env bash

mvn clean package

aws lambda update-function-code \
  --region us-east-1 \
  --function-name helloWorldLambda \
  --zip-file fileb://./target/hello-lambda-1.0-SNAPSHOT.jar