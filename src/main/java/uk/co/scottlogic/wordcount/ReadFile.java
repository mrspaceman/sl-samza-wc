/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package uk.co.scottlogic.wordcount;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReadFile {
    private static final Logger log = LoggerFactory.getLogger(ReadFile.class);

    private final static String TOPIC = "sl-lines";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    long index = 0;

    void runProducer(final String filename) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        index = 0;

        log.info("Reading file [{}]", filename);

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            // for each line in the stream we will send a message into the kafka topic
            Consumer<String> consumerNames = line -> {
                try {
                    //  log.info("sending line [{}]", line);
                    RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC, index++, line.trim())).get();
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            stream.forEach(consumerNames);

        } finally {
            //producer.flush();
            producer.close();
        }
        log.info("Finished reading file [{}]", filename);
    }

    private static void dumpArgs(final String[] args) {
        System.out.println("args are :");
        int argno = 0;
        for (String s : args) {
            System.out.println("\tArg[" + argno + "] " + s);
        }
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a filename");
        } else {
            dumpArgs(args);
            try {
                new ReadFile().runProducer(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}