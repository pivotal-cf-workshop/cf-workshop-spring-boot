#!/bin/bash

set -xe

cd git-assets
mvn -DskipTests package
cp target/cf-workshop-spring-boot-*.jar artifacts/boot-app.jar