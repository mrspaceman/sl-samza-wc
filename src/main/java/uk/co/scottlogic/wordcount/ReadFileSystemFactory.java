package uk.co.scottlogic.wordcount;

import org.apache.samza.config.Config;
import org.apache.samza.metrics.MetricsRegistry;
import org.apache.samza.system.SystemAdmin;
import org.apache.samza.system.SystemConsumer;
import org.apache.samza.util.SinglePartitionWithoutOffsetsSystemAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.samza.system.SystemFactory;
import org.apache.samza.system.SystemProducer;

public class ReadFileSystemFactory implements SystemFactory {
    private static final Logger log = LoggerFactory.getLogger(ReadFileSystemFactory.class);

    @Override
    public SystemConsumer getConsumer(String systemName, Config config, MetricsRegistry metricsRegistry) {

        return new ReadFileConsumer(systemName, metricsRegistry);
    }

    @Override
    public SystemProducer getProducer(String s, Config config, MetricsRegistry metricsRegistry) {
        return null;
    }

    @Override
    public SystemAdmin getAdmin(String s, Config config) {
        return new SinglePartitionWithoutOffsetsSystemAdmin();
    }
}
