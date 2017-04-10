#!/bin/bash

set -xe

#Get the version number from the file passed in as a env var named 'version'
version=`cat $version`

cd git-repo
mvn package
cp target/cf-workshop-spring-boot-*.jar ../artifacts/boot-app-$version.jar