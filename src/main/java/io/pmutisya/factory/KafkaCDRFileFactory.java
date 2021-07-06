package io.pmutisya.factory;

import io.pmutisya.config.ApplicationConfiguration;
import io.pmutisya.config.CDRFileReaderConfiguration;
import io.pmutisya.filereader.DataFileReaderRunnable;
import io.pmutisya.kafkaproducer.KafkaProducer;
import io.pmutisya.repository.CDRFileRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class KafkaCDRFileFactory {
    private static final Logger logger = LogManager.getLogger(DataFileReaderRunnable.class);

    private final ApplicationConfiguration applicationConfiguration;
    private final List<Thread> readerThreads = new ArrayList<>();
    private KafkaProducer kafkaProducer;
    private final CDRFileRepository cdrFileRepository;

    public KafkaCDRFileFactory(ApplicationConfiguration applicationConfiguration, CDRFileRepository cdrFileRepository) {
        this.applicationConfiguration = applicationConfiguration;
        this.cdrFileRepository = cdrFileRepository;
    }

    public void initializeKafkaProducer() {
        kafkaProducer = new KafkaProducer(applicationConfiguration.getKafkaProperties());
    }

    public void initializeReaderThreads() {
        for (CDRFileReaderConfiguration cdrFileReaderConfiguration : applicationConfiguration.getFileReaderConfigurations()) {
            // create thread with that configuration
            DataFileReaderRunnable dataFileReaderRunnable = new DataFileReaderRunnable(cdrFileReaderConfiguration, kafkaProducer);

            Thread thread = new Thread(dataFileReaderRunnable, "reader-thread-" + dataFileReaderRunnable.getDataKey());
            readerThreads.add(thread);
        }
    }

    public void startThreads() throws InterruptedException {
        for (Thread thread : readerThreads) {
            thread.start();
        }
    }

    public void shutDown() {
        logger.info("Stopping reader threads : {}", readerThreads);
        for (Thread thread : readerThreads) {
            thread.interrupt();
        }
        logger.info("Successfully stopped reader threads : {}", readerThreads);

        logger.info("Closing Kafka producer : {}", kafkaProducer);
        this.kafkaProducer.close();
        logger.info("Closed Kafka producer : {}", kafkaProducer);
    }
}
