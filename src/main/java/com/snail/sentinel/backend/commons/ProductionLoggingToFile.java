package com.snail.sentinel.backend.commons;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ProductionLoggingToFile implements LoggingToFile {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public void writeTimeToFileForWarningIterationResult(int numberOfCell, String message) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write(formattedTime + " - Cell number " + numberOfCell + " : " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeTimeToFile(String lineToAdd) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write(formattedTime + " - " + lineToAdd + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
