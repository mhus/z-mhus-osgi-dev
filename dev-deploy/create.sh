#!/bin/bash

VERSION=7.1.0

mvn install -P assembly || exit 1

cd dev-docker
./create.sh $@ || exit 1
cd ..

if [ "$1" = "test" ]; then
    shift
    docker stop dev-playground
    docker rm dev-playground

    docker run -it --name dev-playground \
     -h dev \
     -v ~/.m2:/home/user/.m2 \
     -p 8181:8181 \
     -p 15005:5005 \
     mhus/dev-playground:$VERSION debug
fi

if [ "$1" = "k8s" ]; then
    shift
    kubectl apply -f kubernetes/dev-test.yaml
fi
