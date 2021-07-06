package io.pmutisya.filereader;

import com.hazelcast.core.HazelcastInstance;
import io.pmutisya.config.CDRFileReaderConfiguration;
import io.pmutisya.config.HazelcastConfiguration;
import io.pmutisya.domain.CDRFile;
import io.pmutisya.kafkaproducer.KafkaProducer;
import io.pmutisya.repository.CDRFileRepository;
import io.pmutisya.util.FileReaderUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DataFileReaderRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(DataFileReaderRunnable.class);
    private static final int hazelCastTimeToLiveInHours = 48;
    private static final String hazelCastMapName = DataFileReaderRunnable.class.getName();
    private static final HazelcastInstance hazelcastInstance = HazelcastConfiguration.getHazelcastInstance();

    private final KafkaProducer kafkaProducer;
    private final long startTime = 0;
    private String kafkaTopic = "cdr-files";
    private String folder;
    private String filePattern;
    private boolean checkFileDuplicates = false;
    private String dataKey;
    private Integer headerLine;
    private String headerDelimiter;
    private String recordDelimiter;
    private String recordSkipPattern;
    private String[] headers;
    private int threadSleepTimeMs;

    public DataFileReaderRunnable(CDRFileReaderConfiguration cdrFileReaderConfiguration, KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        readConfigurations(cdrFileReaderConfiguration);

        logger.info("Successfully read configurations : {} into variables. folder : {}, filePattern :  {}, " +
                        "checkFileDuplicates :  {}, dataKey :  {}, headerDelimiter :  {}, headerLine :  {}, " +
                        "recordDelimiter :  {}, recordSkipPattern :  {}, headers :  {}", cdrFileReaderConfiguration,
                folder, filePattern, checkFileDuplicates, dataKey, headerDelimiter, headerLine, recordDelimiter,
                recordSkipPattern, headers);
    }

    private void readConfigurations(CDRFileReaderConfiguration cdrFileReaderConfiguration) {
        folder = cdrFileReaderConfiguration.getFolder();
        filePattern = cdrFileReaderConfiguration.getFilePattern();
        checkFileDuplicates = cdrFileReaderConfiguration.isCheckFileDuplicates();
        dataKey = cdrFileReaderConfiguration.getDataKey();
        headerLine = cdrFileReaderConfiguration.getHeaderLine();
        headerDelimiter = cdrFileReaderConfiguration.getHeaderDelimiter();
        recordDelimiter = cdrFileReaderConfiguration.getRecordDelimiter();
        recordSkipPattern = cdrFileReaderConfiguration.getRecordSkipPattern();
        kafkaTopic = cdrFileReaderConfiguration.getKafkaTopic();
        threadSleepTimeMs = cdrFileReaderConfiguration.getThreadSleepTimeMs();

        String header = cdrFileReaderConfiguration.getHeader();
        if (header != null && !header.isEmpty()) {
            if (headerDelimiter == null || headerDelimiter.isEmpty()) {
                headerDelimiter = "\\s+";
            }

            headers = header.split(headerDelimiter);
        }

        if (recordDelimiter == null) {
            recordDelimiter = "\\s+"; // whitespaces
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                File file = getFileUsingCommons(folder);

                if (file != null) {
                    logger.info("Retrieved file : {}", file);


                    CDRFile cdrFile = createCDRObjectFromFile(file);

                    logger.info("Created CDR Object : {}", cdrFile);

                    int records = readFile(cdrFile);

                    // save cdr file to cache for 48 hours
                    // cdrFileRepository.save(cdrFile);
                    String mapKey = cdrFile.getDataFeed() + "_" + cdrFile.getFilename();
                    hazelcastInstance.getMap(hazelCastMapName).set(mapKey, cdrFile, hazelCastTimeToLiveInHours, TimeUnit.HOURS);
                } else {
                    logger.info("No files to read in folder : {}", folder);
                }

                Thread.sleep(threadSleepTimeMs);

            } catch (InterruptedException e) {
                logger.error("Thread interrupted. Exiting");
                break;
            } catch (Exception e) {
                logger.warn("Encountered exception. Thread proceeding", e);
                try {
                    Thread.sleep(threadSleepTimeMs * 2);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private File getFileUsingCommons(String folder) throws IOException {
        File directory = new File(folder);

        IOFileFilter fileFilter = FileFilterUtils.fileFileFilter();
        if (filePattern != null && !filePattern.isEmpty()) {
            fileFilter = new RegexFileFilter(filePattern);
        }

        List<File> files = (List<File>) FileUtils.listFiles(directory, fileFilter, null);
        files.sort(LastModifiedFileComparator.LASTMODIFIED_REVERSE);

        for (File file : files) {
            if (isDuplicate(file)) {
                logger.info("Found duplicate file : {}. Filename : {}", file, file.getName());

                String duplicateFolder = folder + "/duplicate";

                FileReaderUtil.moveFile(file.getName(), folder, duplicateFolder, StandardCopyOption.REPLACE_EXISTING);
                continue;
            }
            return file;
        }
        return null;
    }

    private File getFile(String folder) throws IOException {
        File dir = new File(folder);
        File[] filesAndDirectories = dir.listFiles();

        logger.debug("Read files and directories : {}", (Object) filesAndDirectories);

        if (filesAndDirectories != null && filesAndDirectories.length > 0) {
            List<File> files = getValidFiles(filesAndDirectories);

            if (!files.isEmpty()) {
                FileReaderUtil.sortFilesByLastModifiedTime(files);
                return files.get(0);
            }
        }
        return null;
    }

    private List<File> getValidFiles(File[] files) throws IOException {
        List<File> filesOnly = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                if (isDuplicate(file)) {
                    logger.info("Found duplicate file : {}. Filename : {}", file, file.getName());

                    String duplicateFolder = folder + "/duplicate";

                    FileReaderUtil.moveFile(file.getName(), folder, duplicateFolder, StandardCopyOption.REPLACE_EXISTING);
                    continue;
                }
                if (!canBeProcessed(file.getName(), filePattern)) {
                    logger.warn("File not for processing : {}. Filename : {}, filePattern : {}", file, file.getName(), filePattern);
                    continue;
                }

                filesOnly.add(file);
            }
        }
        return filesOnly;
    }

    private boolean isDuplicate(File file) {
        if (checkFileDuplicates) {
            String mapKey = dataKey + "_" + file.getName();
            Object cdrFile = hazelcastInstance.getMap(hazelCastMapName).get(mapKey);

            if (cdrFile != null) {
                logger.warn("Found duplicate file : {}", cdrFile);
                return true;
            }
        }
        return false;
    }

    private boolean canBeProcessed(String filename, String filePattern) {
        if (filePattern != null && !filePattern.isEmpty()) {
            return Pattern.matches(filePattern, filename);
        }
        return true;
    }

    private CDRFile createCDRObjectFromFile(File file) {
        // create the file object
        LocalDateTime lastModifiedDateTime = LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, ZoneOffset.ofHours(3));
        LocalDateTime loadingDateTime = LocalDateTime.now();

        return new CDRFile(file.getName(), dataKey, folder, lastModifiedDateTime, file.length(), loadingDateTime);
    }

    private int readFile(CDRFile cdrFile) throws IOException {
        AtomicInteger recordCount = new AtomicInteger(0);

        Path filePath = Paths.get(cdrFile.getWatchFolder() + "/" + cdrFile.getFilename());
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.ISO_8859_1)) {
            AtomicInteger lineNumber = new AtomicInteger(0);
            AtomicInteger validRecords = new AtomicInteger(0);

            AtomicReference<List<String>> inFileHeader = new AtomicReference<>();

            lines.forEach(line -> {
                try {
                    logger.debug("Reading line : {}", line);
                    if (isValid(line, recordSkipPattern)) {
                        lineNumber.incrementAndGet();

                        List<String> record = Arrays.asList(line.split(recordDelimiter));

                        if (headerLine != null && headerLine.equals(lineNumber.get())) {
                            inFileHeader.set(record);
                            return;
                        }


                        Map<String, String> recordMap = recordToMap(record, inFileHeader.get(), headers);
                        logger.debug("Converted record : {} to map : {} using headers : {} and inFileHeader : {}", record, recordMap, headers, inFileHeader);

                        recordMap.put("filename", cdrFile.getFilename());
                        recordMap.put("dataFeed", cdrFile.getDataFeed());

                        recordMap.put("watchFolder", cdrFile.getWatchFolder());
                        recordMap.put("backUpFolder", cdrFile.getBackupFolder());

                        validRecords.incrementAndGet();

                        // save this to Kafka
                        try {
                            kafkaProducer.produceToKafka(recordMap, kafkaTopic, cdrFile.getDataFeed());
                        } catch (Exception e) {
                            logger.warn("Unable to produce record : {} to kafka", recordMap);
                        }
                    }
                    recordCount.incrementAndGet();

                    cdrFile.setTotalRecords(recordCount.get());
                    cdrFile.setTotalLines(lineNumber.get());
                    cdrFile.setTimeProcessed(LocalDateTime.now());
                    cdrFile.setInvalidRecords(recordCount.get() - validRecords.get());
                } catch (Exception ex) {
                    logger.warn("Encountered exception", ex);
                }
            });
        }

        Path backUpPath = Paths.get(cdrFile.getWatchFolder() + "/produced");

        FileReaderUtil.moveFile(filePath, backUpPath, StandardCopyOption.REPLACE_EXISTING);

        return recordCount.get();
    }

    private boolean isValid(String line, String recordSkipPattern) {
        // ignore empty lines
        if (line == null || line.trim().isEmpty()) {
            return false;
        }

        // check if line matches pattern to be skipped
        if (recordSkipPattern != null) {
            try {
                if (Pattern.matches(recordSkipPattern, line)) {
                    return false;
                }
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    private Map<String, String> recordToMap(List<String> record, List<String> inFileHeader, String[] headers) {
        Map<String, String> recordMap = new HashMap<>();
        for (int i = 0; i < record.size(); i++) {
            String field = record.get(i);
            String key = "FIELD_" + i;

            // check headers in file then try checking config
            if (inFileHeader != null && !inFileHeader.isEmpty() && inFileHeader.size() > i) {
                key = inFileHeader.get(i);
            } else if (headers != null && headers.length > i) {
                key = headers[i];
            }

            recordMap.put(key, field);
        }
        return recordMap;
    }

    public String getDataKey() {
        return dataKey;
    }
}
