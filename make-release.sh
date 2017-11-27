#!/bin/bash

NAME=griffin-$(git tag -l | tail -n 1).zip

#build project
mvn clean install

#copy generated jar to the release directory
cp target/griffin.jar release/

#zip the contents of release directory
cd release && zip -r $NAME .
cd .. && mv release/$NAME .

#remove the copied jar from release
rm release/griffin.jar
