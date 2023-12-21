package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.service.JoularAggregateService;
import com.snail.sentinel.backend.service.dto.joular.JoularAggregateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/joular")
public class JoularAggregateResource {
    private final Logger log = LoggerFactory.getLogger(JoularAggregateResource.class);
    private final JoularAggregateService joularAggregateService;

    public JoularAggregateResource(JoularAggregateService joularAggregateService) {
        this.joularAggregateService = joularAggregateService;
    }

    @GetMapping("/aggregates")
    public ResponseEntity<Page<JoularAggregateDTO>> getAllJoularValuesByMethodsForAllCommits(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "1000") int size
    ) {
        log.debug("REST request to get all joular data by methods");
        List<JoularAggregateDTO> joularAggregateDTOList = joularAggregateService.aggregateAll();
        return getPageResponseEntityAggregation(page, size, joularAggregateDTOList);
    }

    @GetMapping("/aggregates/by-commit/{sha}")
    public ResponseEntity<Page<JoularAggregateDTO>> getAllJoularValuesByMethodsForOneCommit(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all joular data by methods for commit : {}", sha);
        List<JoularAggregateDTO> joularAggregateDTOList = joularAggregateService.aggregateAllByCommit(sha);
        return getPageResponseEntityAggregation(page, size, joularAggregateDTOList);
    }

    private ResponseEntity<Page<JoularAggregateDTO>> getPageResponseEntityAggregation(int page, int size, List<JoularAggregateDTO> joularAggregates) {
        if (!joularAggregates.isEmpty()) {
            int totalSize = joularAggregates.size();
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalSize);

            List<JoularAggregateDTO> joularContent = joularAggregates.subList(startIndex, endIndex);
            Page<JoularAggregateDTO> pageContent = new PageImpl<>(joularContent, PageRequest.of(page, size), totalSize);
            return new ResponseEntity<>(pageContent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
