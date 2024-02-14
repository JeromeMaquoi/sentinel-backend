package com.snail.sentinel.backend.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TestingFileListProvider implements FileListProvider {
    private static final Logger log = LoggerFactory.getLogger(TestingFileListProvider.class);

    @Override
    public Set<Path> getFileList(String folder) {
        log.info("testing");
        try {
            Set<Path> fileList = new HashSet<>();
            File resourceFolder = getResourceFolder(folder);
            if (resourceFolder.isDirectory()) {
                for (File file : Objects.requireNonNull(resourceFolder.listFiles())) {
                    fileList.add(file.toPath());
                }
            }
            return fileList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get file list from folder: " + folder, e);
        }
    }

    private File getResourceFolder(String folder) throws URISyntaxException {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(folder)).toURI());
    }
}
