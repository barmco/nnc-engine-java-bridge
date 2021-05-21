#!/bin/bash

###############################################
# THIS BUILD SCRIPT USES FROM NNC-ENGINE ONLY #
#            DO NOT RUN THE SCRIPT            #
###############################################

PREV=$(pwd)

cd "$(dirname "$0")" || return

./gradlew clean
./gradlew build

if [ ! -d "$PREV/.build" ]; then
  mkdir "$PREV/.build"
else
  rm -rf "$PREV/.build"
fi
cp -r ./build/ "$PREV/.build"

ARTIFACT_NAME=$(sed -n 's/^rootProject.name = '"'"'\(.*\)'"'"'$/\1/p' ./settings.gradle)
VERSION_NAME=$(sed -n 's/^version '"'"'\(.*\)'"'"'$/\1/p' ./build.gradle)
JAR_ARCHIVE_NAME="$ARTIFACT_NAME"-"${VERSION_NAME}".jar

mv "$PREV/.build/libs/$JAR_ARCHIVE_NAME" "$PREV/.build/out.jar"
cd "$PREV" || return