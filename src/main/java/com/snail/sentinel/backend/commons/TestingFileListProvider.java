package com.snail.sentinel.backend.commons;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class TestingFileListProvider implements FileListProvider {
    @Override
    public Set<Path> getFileList(String folder) {
        try {
            Set<Path> fileList = new HashSet<>();
            File resourceFolder = getResourceFolder(folder);
            if (resourceFolder != null && resourceFolder.isDirectory()) {
                File[] files = resourceFolder.listFiles((dir, name) -> name.endsWith(".csv"));
                if (files != null) {
                    for (File file : files) {
                        fileList.add(file.toPath());
                    }
                }
            }
            return fileList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get file list from folder: " + folder, e);
        }
    }

    private File getResourceFolder(String folder) throws URISyntaxException {
        return new File(getClass().getClassLoader().getResource(folder).toURI());
    }
}
