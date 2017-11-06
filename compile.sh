#!/bin/bash

docker build -f Dockerfile-build -t sysunite/excel-microservice-build:0.1 .
docker run -it --rm -v $(pwd)/target:/usr/src/app/target sysunite/excel-microservice-build:0.1 package -T 1C -o -Dmaven.test.skip=true
