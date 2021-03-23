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
cd "$PREV" || return