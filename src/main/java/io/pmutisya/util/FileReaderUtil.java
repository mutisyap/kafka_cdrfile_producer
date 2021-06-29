package io.pmutisya.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileReaderUtil {

    public static void sortFilesByLastModifiedTime(List<File> files) {
        try {
            files.sort((f1, f2) -> {
                if (f1 != null && f2 != null && f1.isFile() && f2.isFile()) {
                    return Long.compare(f1.lastModified(), f2.lastModified());
                }
                return 0;
            });
        } catch (Exception ignored) {
        }
    }

    public static void moveFile(String filename, String sourceFolder, String targetFolder, CopyOption copyOption) throws IOException {
        if (!sourceFolder.endsWith("/")) {
            sourceFolder += "/";
        }

        if (!targetFolder.endsWith("/")) {
            targetFolder += "/";
        }

        Path sourceFilePath = Paths.get(sourceFolder + filename);
        Path targetFolderPath = Paths.get(targetFolder);

        moveFile(sourceFilePath, targetFolderPath, copyOption);
    }

    public static void moveFile(Path sourceFilePath, Path targetFolderPath, CopyOption copyOption) throws IOException {
        if (copyOption == null) {
            copyOption = StandardCopyOption.REPLACE_EXISTING;
        }

        Files.createDirectories(targetFolderPath);

        Path tempPath = targetFolderPath.resolve(sourceFilePath.getFileName());

        Files.move(sourceFilePath, tempPath, copyOption);
    }
}
