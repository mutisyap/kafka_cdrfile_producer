package io.pmutisya.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_cdr_files")
public class CDRFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id", nullable = false)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "data_feed")
    private String dataFeed;

    @Column(name = "watch_folder")
    private String watchFolder;

    @Column(name = "backup_folder")
    private String backupFolder;

    @Column(name = "total_records")
    private Integer totalRecords;

    @Column(name = "invalid_records")
    private Integer invalidRecords;

    @Column(name = "total_lines")
    private Integer totalLines;

    @Column(name = "last_modified")
    private LocalDateTime lastModified; // metadata

    @Column(name = "size")
    private long size; // in bytes/kbs

    @Column(name = "time_loaded")
    private LocalDateTime timeLoaded;

    @Column(name = "time_processed")
    private LocalDateTime timeProcessed;

    @Column(name = "time_moved")
    private LocalDateTime timeMoved;

    @Column(name = "max_record_time")
    private Integer maxRecordTime;

    @Column(name = "min_record_time")
    private Integer minRecordTime;

    @Column(name = "avg_record_time")
    private Integer avgRecordTime;

    public CDRFile() {
    }

    public CDRFile(String filename, String dataFeed, String watchFolder, String backupFolder, Integer totalRecords, Integer invalidRecords, Integer totalLines, LocalDateTime lastModified, long size, LocalDateTime timeLoaded, LocalDateTime timeProcessed, LocalDateTime timeMoved, Integer maxRecordTime, Integer minRecordTime, Integer avgRecordTime) {
        this.filename = filename;
        this.dataFeed = dataFeed;
        this.watchFolder = watchFolder;
        this.backupFolder = backupFolder;
        this.totalRecords = totalRecords;
        this.invalidRecords = invalidRecords;
        this.totalLines = totalLines;
        this.lastModified = lastModified;
        this.size = size;
        this.timeLoaded = timeLoaded;
        this.timeProcessed = timeProcessed;
        this.timeMoved = timeMoved;
        this.maxRecordTime = maxRecordTime;
        this.minRecordTime = minRecordTime;
        this.avgRecordTime = avgRecordTime;
    }


    public CDRFile(String filename, String dataFeed, String watchFolder, LocalDateTime lastModified, long size, LocalDateTime timeLoaded) {
        this.filename = filename;
        this.dataFeed = dataFeed;
        this.watchFolder = watchFolder;
        this.lastModified = lastModified;
        this.size = size;
        this.timeLoaded = timeLoaded;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDataFeed() {
        return dataFeed;
    }

    public void setDataFeed(String dataFeed) {
        this.dataFeed = dataFeed;
    }

    public String getWatchFolder() {
        return watchFolder;
    }

    public void setWatchFolder(String watchFolder) {
        this.watchFolder = watchFolder;
    }

    public String getBackupFolder() {
        return backupFolder;
    }

    public void setBackupFolder(String backupFolder) {
        this.backupFolder = backupFolder;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getInvalidRecords() {
        return invalidRecords;
    }

    public void setInvalidRecords(Integer invalidRecords) {
        this.invalidRecords = invalidRecords;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getTimeLoaded() {
        return timeLoaded;
    }

    public void setTimeLoaded(LocalDateTime timeLoaded) {
        this.timeLoaded = timeLoaded;
    }

    public LocalDateTime getTimeProcessed() {
        return timeProcessed;
    }

    public void setTimeProcessed(LocalDateTime timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    public LocalDateTime getTimeMoved() {
        return timeMoved;
    }

    public void setTimeMoved(LocalDateTime timeMoved) {
        this.timeMoved = timeMoved;
    }

    public Integer getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(Integer totalLines) {
        this.totalLines = totalLines;
    }

    public Integer getMaxRecordTime() {
        return maxRecordTime;
    }

    public void setMaxRecordTime(Integer maxRecordTime) {
        this.maxRecordTime = maxRecordTime;
    }

    public Integer getMinRecordTime() {
        return minRecordTime;
    }

    public void setMinRecordTime(Integer minRecordTime) {
        this.minRecordTime = minRecordTime;
    }

    public Integer getAvgRecordTime() {
        return avgRecordTime;
    }

    public void setAvgRecordTime(Integer avgRecordTime) {
        this.avgRecordTime = avgRecordTime;
    }

    @Override
    public String toString() {
        return "CDRFile{" +
                "filename='" + filename + '\'' +
                ", dataFeed='" + dataFeed + '\'' +
                ", watchFolder='" + watchFolder + '\'' +
                ", backupFolder='" + backupFolder + '\'' +
                ", totalRecords=" + totalRecords +
                ", invalidRecords=" + invalidRecords +
                ", totalLines=" + totalLines +
                ", lastModified=" + lastModified +
                ", size=" + size +
                ", timeLoaded=" + timeLoaded +
                ", timeProcessed=" + timeProcessed +
                ", timeMoved=" + timeMoved +
                ", maxRecordTime=" + maxRecordTime +
                ", minRecordTime=" + minRecordTime +
                ", avgRecordTime=" + avgRecordTime +
                '}';
    }
}
