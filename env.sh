#!/bin/bash

export XF_TERM='xfce4-terminal --tab '

export SAMZA_HOME_DIR=/home/aaspellc/samza
export SAMZA_ROOT_DIR=$SAMZA_HOME_DIR/hello-samza
export DEPLOY_ROOT_DIR=$SAMZA_ROOT_DIR/deploy

export ZK_DIR=$DEPLOY_ROOT_DIR/zookeeper
export KF_DIR=$DEPLOY_ROOT_DIR/kafka
export SA_DIR=$DEPLOY_ROOT_DIR/samza
export YN_DIR=$DEPLOY_ROOT_DIR/yarn


export ZK_DIR=$SAMZA_HOME_DIR/zookeeper-3.4.12
export KF_DIR=$SAMZA_HOME_DIR/kafka_2.11-1.1.0
export YN_DIR=$SAMZA_HOME_DIR/hadoop-3.0.3
#export HADOOP_YARN_HOME=$YN_DIR

export ZOOKEEPER_PORT=2181
export RESOURCEMANAGER_PORT=8032
export NODEMANAGER_PORT=8042
export KAFKA_PORT=9092

export PRJ_ROOT=~/src/wordcount/wcsamza
export YN_DIR=~/bin/hadoop-3.0.3
export KF_DIR=~/bin/kafka_2.11-1.1.0
export ZK_DIR=~/bin/zookeeper-3.4.12


if false; then

    $ZK_DIR/bin/zkServer.sh start-foreground $ZK_DIR/conf/zoo.cfg

    $YN_DIR/bin/yarn --daemon start resourcemanager
    $YN_DIR/bin/yarn --daemon start nodemanager

    $KF_DIR/bin/kafka-server-start.sh $KF_DIR/config/server.properties

    $KF_DIR/bin/kafka-topics.sh --zookeeper localhost:2181 --list
    $KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic sl-lines
    $KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic sl-words --from-beginning
    $KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic sl-wordtotals --from-beginning

fi
