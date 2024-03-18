#!/bin/bash
#vi /etc/crontab		设置定时任务频率
#*/5 * * * * root /data/facepay/auto.sh > /data/facepay/auto.log 2>&1
source /etc/profile
TIME=$(date "+%Y-%m-%d %H:%M:%S")
APP_NAME=spring-boot-tuixiuwuyou-1.0.jar
echo "-------${TIME}-------cheack application:"${APP_NAME}
PID=`ps -ef |grep $APP_NAME | grep -v grep | awk '{print $2}'`
if [ -n "$PID" ];then
  echo "$APP_NAME:is already running,PID=$PID"
else
    exec nohup /home/user0/jdk/jdk1.8.0_221/bin/java -jar /home/user0/project/tuixiuwuyou/demo-0.0.1-SNAPSHOT.jar >> /home/user0/project/tuixiuwuyou/apl.log 2>&1 &
    PID=`ps -ef |grep $APP_NAME | grep -v grep | awk '{print $2}'`
    echo "PID:${PID}"
    count=0
    while [ -z "$PID" ]
    do
      if (($count == 10));then
        echo "$APP_NAME:$(expr $count \* 10) start timeout!"
        break
      fi
      count=$(($count+1))
      echo "$APP_NAME starting..................$count"
      sleep 10s
      PID=`ps -ef |grep $APP_NAME | grep -v grep | awk '{print $2}'`
      echo "PID:${PID}"
  done
  echo "$APP_NAME:start success,PID=$PID"
  fi
  echo ""
