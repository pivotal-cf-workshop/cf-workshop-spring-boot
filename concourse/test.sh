#!/bin/bash -e
echo "v1"
set -xe

cat $version
version=`cat $version`
echo "derek"
echo $version

touch ../artifacts/boot-app-$version.jar
