package com.snail.sentinel.backend.commons;

import java.nio.file.Path;
import java.util.Set;

public interface FileListProvider {
    Set<Path> getFileList(String folder);
}
