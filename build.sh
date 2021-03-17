#!/usr/bin/env bash
# Filename: build.sh
echo Accessing libs folder to delete previous versions
sleep .5
cd Docker/libs
sleep .5
echo Directory $(pwd) was accessed successfuly.
sleep .7
echo Deleting anything here
sleep 2
# shellcheck disable=SC2035
rm -rf *
echo I think that everything was deleted... or not
sleep .5
echo Lets generate the JARs
sleep .7
echo Accessing relay folder
sleep .5
cd ../../relay
sleep .7
echo directory $(pwd) was accessed successfuly.
sleep .5
echo Generating relay JAR
sleep .7
./gradlew build
sleep .7
echo --------------------------------------------------------------
echo yf builded builded, yf not builded it will not build anymore /
echo --------------------------------------------------------------
sleep 1
echo Lets build consumer too
sleep .5
echo Accessing consumer folder
cd ../consumer
sleep .5
echo directory $(pwd) was accessed successfuly.
sleep .5
echo Generating consumer JAR
sleep .7
./gradlew build
sleep .7
echo --------------------------------------------------------------
echo yf builded builded, yf not builded it will not build anymore /
echo --------------------------------------------------------------
sleep 1
echo Lets move both to build/libs folder
sleep .5
# shellcheck disable=SC2103
cd ..
echo $(pwd)
mv relay/build/libs/* Docker/libs
mv consumer/build/libs/* Docker/libs
sleep 1
echo Everything moved, lets create the images
cd Docker
echo creating relay image
sleep 1
docker build -f RelayDockerfile -t spring/relay:latest .
sleep 1
echo creating consumer image
sleep 1
docker build -f ConsumerDockerfile -t spring/consumer:latest .
echo images created successfuly, update the compose