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

import org.apache.samza.config.Config;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.InitableTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskContext;
import org.apache.samza.task.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitLineTask implements StreamTask, InitableTask {
    private static final Logger log = LoggerFactory.getLogger(SplitLineTask.class);
    private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka", "sl-words");

    public SplitLineTask() {
        log.info("SplitLineTask Constructing");
    }

    @Override
    public void init(Config config, TaskContext taskContext) throws Exception {
        log.info("SplitLineTask init()");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
        String message = (String) envelope.getMessage();
        log.info("processing line [{}]", message);
        String[] words = message.split("\\s+"); // split line on one or more whitespace
        for (String word : words) {
            log.info("emitting word : {}", word);
            try {
                collector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM, word, word));
            } catch (Exception e) {
                log.info("Exception sending msg : {}", e.getMessage());
            }
        }
    }

}