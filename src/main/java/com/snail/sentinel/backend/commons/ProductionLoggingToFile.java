package com.snail.sentinel.backend.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ProductionLoggingToFile implements LoggingToFile {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
    private Logger log = LoggerFactory.getLogger(ProductionLoggingToFile.class);

    @Override
    public void writeTimeToFileForWarningIterationResult(int numberOfCell, String message) {
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write(formattedTime + " - Cell number " + numberOfCell + " : " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeTimeToFileUnhandledMethods(String lineToAdd) {
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            log.warn(lineToAdd);
            fileWriter.write(formattedTime + " - " + lineToAdd + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeEmptyLineToFile() {
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
