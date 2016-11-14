#!/bin/bash

set -xe

cd git-repo
mvn -DskipTests package
cp target/cf-workshop-spring-boot-*.jar artifacts/boot-app.jar