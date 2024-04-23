package com.snail.sentinel.backend;

import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import com.snail.sentinel.backend.service.exceptions.CSVFileNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectDataReaderTest {
    @Test
    void readProjectsFromCSVTest() throws CSVFileNotFoundException {
        String csvFilePath = "src/test/resources/project-data-test/projects-data.csv";
        ProjectDataReader projectDataReader = new ProjectDataReader();
        List<RepoDataDTO> maybeRepoDataDTOList = projectDataReader.readProjectsFromCSV(csvFilePath);

        RepoDataDTO repoDataDTO1 = new RepoDataDTO("apache", "commons-configuration", "59e5152722198526c6ffe5361de7d1a6a87275c7");
        RepoDataDTO repoDataDTO2 = new RepoDataDTO("spring-projects", "spring-boot", "3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21");
        RepoDataDTO repoDataDTO3 = new RepoDataDTO("hibernate", "hibernate-orm","12442bd8c7cde6e7c006a6277eeb8e81ad0c2219");
        RepoDataDTO repoDataDTO4 = new RepoDataDTO("Jabref", "jabref", "5c9d8989f968d0ee3a942b411ef7fe121ed94609");
        RepoDataDTO repoDataDTO5 = new RepoDataDTO("INRIA", "spoon", "066f4cf207359e06d30911a553dedd054aef595c");

        assertThat(maybeRepoDataDTOList).containsExactlyInAnyOrder(repoDataDTO1, repoDataDTO2, repoDataDTO3, repoDataDTO4, repoDataDTO5);
    }
}
