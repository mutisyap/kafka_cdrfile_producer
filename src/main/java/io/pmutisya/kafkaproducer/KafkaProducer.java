package io.pmutisya.kafkaproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pmutisya.filereader.DataFileReaderRunnable;
import io.pmutisya.util.MapSerializerUtil;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Properties;

public class KafkaProducer {
    private static final Logger logger = LogManager.getLogger(DataFileReaderRunnable.class);

    private final Producer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducer(Properties kafkaProperties) {
        this.kafkaProducer = new org.apache.kafka.clients.producer.KafkaProducer<>(kafkaProperties);
        logger.info("Successfully initialized Kafka producer. Kafka Properties : {}", kafkaProperties);
    }

    public void produceToKafka(Object object, String topic, String key) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(object);
        produceToKafka(jsonString, topic, key);
    }

    public void produceToKafka(Map<String, String> recordMap, String topic, String key) throws JsonProcessingException {
        String jsonString = MapSerializerUtil.serializeMapToString(recordMap);
        produceToKafka(jsonString, topic, key);
    }

    public void produceToKafka(String record, String topic, String key) {
        logger.debug("About to produce record : {} to kafka topic : {} with key : {}", record, topic, key);
        ProducerRecord<String, String> customerRecord = new ProducerRecord<>(topic, key, record);
        this.kafkaProducer.send(customerRecord);
        logger.debug("Successfully produced record : {} to kafka topic : {} with key : {}", record, topic, key);
    }

    public void close() {
        this.kafkaProducer.close();
    }
}
