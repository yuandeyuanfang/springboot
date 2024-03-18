#!/bin/bash
source /etc/profile
rm -f tpid

nohup /home/user0/jdk/jdk1.8.0_221/bin/java -jar /home/user0/project/tuixiuwuyou/demo-0.0.1-SNAPSHOT.jar  > ocr.log 2>&1 &

echo $! > tpid

echo Start Success!
