package uk.co.scottlogic.wordcount;

import org.apache.samza.application.StreamApplication;
import org.apache.samza.config.Config;
import org.apache.samza.operators.MessageStream;
import org.apache.samza.operators.StreamGraph;
import org.apache.samza.serializers.NoOpSerde;

public class ReadFileApplication implements StreamApplication {
    @Override
    public void init(StreamGraph streamGraph, Config config) {
        MessageStream<String> readfileEvents = streamGraph.getInputStream("en-slreadfile", new NoOpSerde<>());


    }
}