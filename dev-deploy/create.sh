#!/bin/bash
#
# Copyright (C) 2020 Mike Hummel (mh@mhus.de)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


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
