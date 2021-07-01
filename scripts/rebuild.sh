#!/bin/sh 

cd ..

echo "Started packaging"

mvn clean package

echo "Done packaging" 

cd scripts

exit 0;
