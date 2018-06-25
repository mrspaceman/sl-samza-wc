#!/bin/bash

. ./env.sh

mkdir -p $KF_DIR
tar -xf ~/Downloads/kafka_2.11-1.1.0.tgz -C $KF_DIR/..

echo -e "Starting Kafka ..."
$XF_TERM --title="Kafka Server" -e "$KF_DIR/bin/kafka-server-start.sh $KF_DIR/config/server.properties"

#read -n 1 -s -r -p "Press any key to continue"
#echo -e "\n"

