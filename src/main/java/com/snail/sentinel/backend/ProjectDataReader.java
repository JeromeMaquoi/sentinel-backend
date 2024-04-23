package com.snail.sentinel.backend;

import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import com.snail.sentinel.backend.service.exceptions.CSVFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProjectDataReader {
    Logger log = LoggerFactory.getLogger(ProjectDataReader.class);

    public ProjectDataReader(){}

    public List<RepoDataDTO> readProjectsFromCSV(String csvFilePath) throws CSVFileNotFoundException {
        List<RepoDataDTO> repoDataDTOList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == 3) {
                    RepoDataDTO repoDataDTO = new RepoDataDTO(split[0].trim(), split[1].trim(), split[2].trim());
                    repoDataDTOList.add(repoDataDTO);
                } else {
                    log.error("Invalid data format in CSV file {} for line {}", csvFilePath, line);
                }
            }
            return repoDataDTOList;

        } catch (FileNotFoundException e) {
            throw new CSVFileNotFoundException(csvFilePath, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
