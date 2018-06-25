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

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.InitableTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;
import org.apache.samza.task.WindowableTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WordCountTask implements StreamTask, InitableTask, WindowableTask {
    private static final Logger log = LoggerFactory.getLogger(WordCountTask.class);
    private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka", "sl-wordtotals");

    private Map<String, Integer> wordCountsWindwd = new HashMap<String, Integer>();

    public WordCountTask() {
        log.info("WordCountTask Constructing");
    }

    @Override
    public void init(org.apache.samza.config.Config config, org.apache.samza.task.TaskContext context) throws Exception {
        log.info("SplitLineTask init()");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(IncomingMessageEnvelope envelope, MessageCollector collector, TaskCoordinator coordinator) {
        String word = (String) envelope.getMessage();
        //   log.info("processing word [{}]", word);
        Integer count = wordCountsWindwd.get(word.toLowerCase());
        if (count == null) count = 0;
        count++;
        wordCountsWindwd.put(word.toLowerCase(), count);
    }

    @Override
    public void window(org.apache.samza.task.MessageCollector collector, org.apache.samza.task.TaskCoordinator coordinator) throws Exception {
        // send wordcounts to stream
        try {
            for (String key : wordCountsWindwd.keySet()) {
                collector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM,
                        key,
                        key + ":" + wordCountsWindwd.get(key)));
            }
        } catch (Exception e) {
            log.info("Exception sending msg : {}", e.getMessage());
        }

        // Reset wordcounts after windowing.
        wordCountsWindwd = new HashMap<String, Integer>();
    }
}

