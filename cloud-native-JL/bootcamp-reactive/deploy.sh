#! /usr/bin/env bash

NS=cnj
APP_NAME=customers
IMAGE_NAME=ghcr.io/omar-p/cnj-basics:latest

#mvn -DskipTests=true clean package spring-boot:build-image \
#  -Dspring-boot.build-image.imageName=$IMAGE_NAME

mkdir -p k8s
kubectl get ns/$NS || kubectl create  -f k8s/ns.yaml
kubectl get deploy/$APP_NAME -n $NS || \
  kubectl -n $NS  create -f k8s/deploy.yaml
kubectl get svc/$APP_NAME -n $NS || \
  kubectl -n $NS  create -f k8s/svc.yaml