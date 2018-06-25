package uk.co.scottlogic.wordcount;

import org.apache.samza.Partition;
import org.apache.samza.metrics.MetricsRegistry;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.SystemStreamPartition;
import org.apache.samza.util.BlockingEnvelopeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadFileConsumer extends BlockingEnvelopeMap {
    private static final Logger log = LoggerFactory.getLogger(ReadFileConsumer.class);
    private final String systemName;

    public ReadFileConsumer(String systemName, MetricsRegistry metricsRegistry) {
        this.systemName = systemName;
    }

    @Override
    public void start() {
        SystemStreamPartition systemStreamPartition = new SystemStreamPartition(systemName, "sl-lines", new Partition(0));

        String fileline = "will this actually go anywhere";
        try {
            put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null, fileline));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public void stop() {

    }
}
