package io.pmutisya.config;

public class CDRFileReaderConfiguration {

    private String folder;

    private String filePattern;

    private boolean checkFileDuplicates;

    private String dataKey;

    private String header;

    private Integer headerLine;

    private String headerDelimiter;

    private String recordDelimiter;

    private String recordSkipPattern;

    private String kafkaTopic;

    private int threadSleepTimeMs;

    private int eventsPerSecond;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public boolean isCheckFileDuplicates() {
        return checkFileDuplicates;
    }

    public void setCheckFileDuplicates(boolean checkFileDuplicates) {
        this.checkFileDuplicates = checkFileDuplicates;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getHeaderLine() {
        return headerLine;
    }

    public void setHeaderLine(Integer headerLine) {
        this.headerLine = headerLine;
    }

    public String getHeaderDelimiter() {
        return headerDelimiter;
    }

    public void setHeaderDelimiter(String headerDelimiter) {
        this.headerDelimiter = headerDelimiter;
    }

    public String getRecordDelimiter() {
        return recordDelimiter;
    }

    public void setRecordDelimiter(String recordDelimiter) {
        this.recordDelimiter = recordDelimiter;
    }

    public String getRecordSkipPattern() {
        return recordSkipPattern;
    }

    public void setRecordSkipPattern(String recordSkipPattern) {
        this.recordSkipPattern = recordSkipPattern;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public int getThreadSleepTimeMs() {
        return threadSleepTimeMs;
    }

    public void setThreadSleepTimeMs(int threadSleepTimeMs) {
        this.threadSleepTimeMs = threadSleepTimeMs;
    }

    public int getEventsPerSecond() {
        return eventsPerSecond;
    }

    public void setEventsPerSecond(int eventsPerSecond) {
        this.eventsPerSecond = eventsPerSecond;
    }

    @Override
    public String toString() {
        return "CDRFileReaderConfiguration{" +
                "folder='" + folder + '\'' +
                ", filePattern='" + filePattern + '\'' +
                ", checkFileDuplicates=" + checkFileDuplicates +
                ", dataKey='" + dataKey + '\'' +
                ", header='" + header + '\'' +
                ", headerLine=" + headerLine +
                ", headerDelimiter='" + headerDelimiter + '\'' +
                ", recordDelimiter='" + recordDelimiter + '\'' +
                ", recordSkipPattern='" + recordSkipPattern + '\'' +
                ", kafkaTopic='" + kafkaTopic + '\'' +
                ", threadSleepTimeMs=" + threadSleepTimeMs +
                ", eventsPerSecond=" + eventsPerSecond +
                '}';
    }
}
