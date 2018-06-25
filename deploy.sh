#!/bin/bash

. ./env.sh

    mvn dependency:sources


    rm -rf $PRJ_ROOT/logs/*
    rm -rf $PRJ_ROOT/tmp/*
    mvn clean package

    read -n 1 -s -r -p "Press any key to continue"
    echo -e "\n"

    mkdir -p $PRJ_ROOT/logs
    tar xvf $PRJ_ROOT/target/wc-samza-1.0-SNAPSHOT-dist.tar.gz -C $PRJ_ROOT/tmp/

    read -n 1 -s -r -p "Press any key to continue"
    echo -e "\n"

    export JAVA_OPTS="$JAVA_OPTS -Dsamza.log.dir=$PRJ_ROOT/logs"
    $PRJ_ROOT/tmp/bin/run-job.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://$PRJ_ROOT/tmp/config/sl-splittask.properties
    read -n 1 -s -r -p "Press any key to continue"
    echo -e "\n"

    $PRJ_ROOT/tmp/bin/list-yarn-job.sh

    $KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic sl-lines --from-beginning

    $KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic sl-words --from-beginning


    cd $PRJ_ROOT/tmp/ && $PRJ_ROOT/tmp/bin/run-class.sh uk.co.scottlogic.wordcount.ReadFile /usr/lib/jvm/java-8-openjdk-amd64/docs/copyright && cd $PRJ_ROOT


