package com.snail.sentinel.backend.commons;

import com.snail.sentinel.backend.service.exceptions.NoCsvLineFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductionFileListProvider implements FileListProvider {
    private static final Logger log = LoggerFactory.getLogger(ProductionFileListProvider.class);
    @Override
    public Set<Path> getFileList(String folder) {
        log.info("folder : {}", folder);
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            return stream
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
    }
}
