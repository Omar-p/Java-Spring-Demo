#!/usr/bin/env bash

export ROLE_ARN=arn:aws:iam::658262327113:role/aws_lambda_basic

aws lambda create-function \
  --region us-east-1 \
  --function-name helloWorldLambda \
  --zip-file fileb://./target/hello-lambda-1.0-SNAPSHOT.jar \
  --role $ROLE_ARN \
  --handler io.example.HelloWorld::handleRequest \
  --runtime java17 \
  --timeout 15 \
  --memory-size 512

# fileb we're referencing a binary file , supposed to be an object in S3