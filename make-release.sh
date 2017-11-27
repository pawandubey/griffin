#!/bin/bash

#build project
mvn clean install

#copy generated jar to the release directory
cp target/griffin.jar release/

#zip the contents of release directory
zip -r griffin-$(git tag -l | tail -n 1).zip release

#remove the copied jar from release
rm release/griffin.jar
