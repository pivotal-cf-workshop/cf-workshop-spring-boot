#!/bin/bash -e
echo "v2"
set -xe

cat $version
version=`cat $version`
echo "derek"
echo $version

touch ../artifacts/boot-app-$version.jar
