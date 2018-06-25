#!/bin/bash

. ./env.sh

mkdir -p $YN_DIR

echo -e "Extracting Yarn ..."
tar -xf ~/Downloads/hadoop-3.0.3.tar.gz -C $YN_DIR/..
#mv $YN_DIR/../hadoop-3.0.3 $YN_DIR/
mkdir -p $HOME/.samza/conf
cp ./yarn-site.xml $HOME/.samza/conf/
cp ./yarn-site.xml $YN_DIR/etc/hadoop/

echo -e "Starting Yarn ..."
$YN_DIR/sbin/yarn-daemon.sh start resourcemanager
$YN_DIR/sbin/yarn-daemon.sh start nodemanager


