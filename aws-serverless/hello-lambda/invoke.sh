#!/usr/bin/env bash

aws lambda invoke \
  --function-name helloWorldLambda \
  --region us-east-1 \
  --payload '"omar"' \
  --invocation-type RequestResponse \
  --cli-binary-format raw-in-base64-out out.txt
