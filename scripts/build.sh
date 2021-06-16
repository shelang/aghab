#!/usr/bin/env bash

export VERSION=$1

./mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
docker build -t shelang/aghab:$VERSION -f src/main/docker/Dockerfile.native .