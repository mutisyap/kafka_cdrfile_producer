package io.pmutisya.config;

import java.util.List;
import java.util.Properties;

public class ApplicationConfiguration {
    private Properties kafkaProperties;
    private List<CDRFileReaderConfiguration> fileReaderConfigurations;

    public Properties getKafkaProperties() {
        return kafkaProperties;
    }

    public void setKafkaProperties(Properties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public List<CDRFileReaderConfiguration> getFileReaderConfigurations() {
        return fileReaderConfigurations;
    }

    public void setFileReaderConfigurations(List<CDRFileReaderConfiguration> fileReaderConfigurations) {
        this.fileReaderConfigurations = fileReaderConfigurations;
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "kafkaProperties=" + kafkaProperties +
                ", fileReaderConfigurations=" + fileReaderConfigurations +
                '}';
    }
}
