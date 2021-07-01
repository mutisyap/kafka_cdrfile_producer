#!/bin/sh

if [ `ps -ef | grep java | grep -v grep | grep "KCP Service" |wc -l` -eq 1 ]
then
	echo "KCP Service is already running. "
else
  echo "KCP Service starting. Please wait..."
  java -cp "target/*:target/libs/*" io.pmutisya.App "KCP Service" &
	echo "KCP Service started. "
fi
exit 0;
