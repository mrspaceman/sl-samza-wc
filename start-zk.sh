#!/bin/bash

. ./env.sh

rm -rf /var/zookeeper/*

echo "make [$ZK_DIR] directory" 
mkdir -p $ZK_DIR
tar -xf ~/Downloads/zookeeper-3.4.12.tar.gz -C $ZK_DIR/..

sed 's|/tmp/zookeeper|/var/zookeeper|' <$ZK_DIR/conf/zoo_sample.cfg >$ZK_DIR/conf/zoo.cfg
ls -lCF $ZK_DIR/conf/zoo.cfg
sync

echo -e "Starting Zookeeper ..."
$XF_TERM --title="ZooKeeper" -e "$ZK_DIR/bin/zkServer.sh start-foreground $ZK_DIR/conf/zoo.cfg"


$XF_TERM --title="ZooKeeper CLI" -e "$ZK_DIR/bin/zkCli.sh"

#read -n 1 -s -r -p "Press any key to continue"
#echo -e "\n"
