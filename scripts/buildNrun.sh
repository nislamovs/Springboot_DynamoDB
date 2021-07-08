#!/usr/bin/env bash

# export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=/usr/lib/jvm/java-15-oracle

#Cleanup
./stop_n_remove_containers.sh ;
./docker_total_cleanup.sh ;

#Stop on error
set -e ;

#Go to project root folder
cd .. ;

#Build microservice and its docker image
./gradlew clean build docker -x test;

#Launch all docker chain
docker-compose up ;
