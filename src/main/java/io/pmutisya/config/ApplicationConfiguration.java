package io.pmutisya.config;

import java.util.List;
import java.util.Properties;

public class ApplicationConfiguration {
    private String applicationName;

    private String moduleName;

    private String mulikaURL;

    private String mulikaApiKey;

    private int reportStatsIntervalMs;

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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMulikaURL() {
        return mulikaURL;
    }

    public void setMulikaURL(String mulikaURL) {
        this.mulikaURL = mulikaURL;
    }

    public String getMulikaApiKey() {
        return mulikaApiKey;
    }

    public void setMulikaApiKey(String mulikaApiKey) {
        this.mulikaApiKey = mulikaApiKey;
    }

    public int getReportStatsIntervalMs() {
        return reportStatsIntervalMs;
    }

    public void setReportStatsIntervalMs(int reportStatsIntervalMs) {
        this.reportStatsIntervalMs = reportStatsIntervalMs;
    }

    @Override
    public String toString() {
        return "ApplicationConfiguration{" +
                "applicationName='" + applicationName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", mulikaURL='" + mulikaURL + '\'' +
                ", mulikaApiKey='" + mulikaApiKey + '\'' +
                ", reportStatsIntervalMs=" + reportStatsIntervalMs +
                ", kafkaProperties=" + kafkaProperties +
                ", fileReaderConfigurations=" + fileReaderConfigurations +
                '}';
    }
}
