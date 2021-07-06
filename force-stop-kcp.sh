#!/bin/sh

if [ `ps -ef | grep java | grep -v grep | grep "KCP Service" |wc -l` -eq 1 ]
	then
		echo "Stopping KCP service..."
		sleep 2;
		ps -ef | grep java | grep -v grep | grep "KCP Service" | awk {'print$2'}| xargs kill -9
		echo "Stopped KCP Service"
	else 
		echo 'KCP Service is not running '
fi 

exit 0; 
