#!/bin/bash

#
# git clone https://git.apache.org/samza-hello-samza.git hello-samza
# cd hello-samza
#

. ./env.sh

install(){
	rm -rf /tmp/*
	rm -rf ~/.samza/
	rm -rf /var/zookeeper/*
	rm -rf $SAMZA_ROOT_DIR/deploy

	echo -e "Installing Packages..."
	$SAMZA_ROOT_DIR/bin/grid install samza
	$SAMZA_ROOT_DIR/bin/grid install zookeeper
	$SAMZA_ROOT_DIR/bin/grid install kafka

	sed 's|/tmp/zookeeper|/var/zookeeper|' <$ZK_DIR/conf/zoo.cfg >/tmp/zoo.cfg
	cp /tmp/zoo.cfg $ZK_DIR/conf/zoo.cfg
}

# install


mkdir -p $SAMZA_ROOT_DIR/logs
mkdir -p $SA_DIR


./start-zk.sh

./start-yn.sh

./start-kf.sh


echo -e "Building..."
cd hello-samza 
mvn -Drat.ignoreErrors=true -e package
tar -xvf $SAMZA_ROOT_DIR/target/hello-samza-0.14.0-dist.tar.gz -C $SA_DIR
cd ..

#
# deploy a Samza job which listens to wikipedia API, receives the feed in realtime and produces the feed to the Kafka topic wikipedia-raw
#
echo -e "Deploy a Samza job..."
# $XF_TERM --title="Hello-Samza" -e "$SA_DIR/bin/run-app.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://$SA_DIR/config/wikipedia-feed.properties"


echo -e "Produce Dummy edits..."
$XF_TERM --title="Emit Dummy edits" -e "$SAMZA_ROOT_DIR/bin/produce-wikipedia-raw-data.sh" 

$XF_TERM --title="kafka con-raw" -e "$KF_DIR/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic wikipedia-raw"
read -n 1 -s -r -p "Press any key to continue"
echo -e "\n"


# Display some stats
$XF_TERM --title="Hello-Samza Parser" -e "$SA_DIR/bin/run-app.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://$SA_DIR/config/wikipedia-parser.properties"
$XF_TERM --title="Hello-Samza Stats"  -e "$SA_DIR/bin/run-app.sh --config-factory=org.apache.samza.config.factories.PropertiesConfigFactory --config-path=file://$SA_DIR/config/wikipedia-stats.properties"
read -n 1 -s -r -p "Press any key to continue"
echo -e "\n"


$XF_TERM --title="kafka con-edits" -e "$KF_DIR/bin/kafka-console-consumer.sh  --zookeeper localhost:2181 --topic wikipedia-edits"
$XF_TERM --title="kafka con-stats" -e "$KF_DIR/bin/kafka-console-consumer.sh  --zookeeper localhost:2181 --topic wikipedia-stats"




