package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.impl.JoularServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class JoularEntityResource {
    private final Logger log = LoggerFactory.getLogger(JoularEntityResource.class);
    private final JoularEntityRepository joularEntityRepository;

    private JoularServiceImpl joularService;

    public JoularEntityResource(JoularEntityRepository joularEntityRepository, JoularServiceImpl joularService) {
        this.joularEntityRepository = joularEntityRepository;
        this.joularService = joularService;
    }

    @GetMapping("/joular-entities")
    public Page<JoularEntity> getAllJoularEntities(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all JoularEntities");
        PageRequest pageRequest = PageRequest.of(page, size);
        return joularEntityRepository.findAll(pageRequest);
    }

    @GetMapping("/joular-entities/by-commit/{sha}")
    public ResponseEntity<List<JoularEntity>> getAllJoularDataFromOneCommit(@PathVariable String sha) {
        log.debug("REST request to get all joular data from commit : {}", sha);
        List<JoularEntity> joularEntities = joularService.findByCommitSha(sha);
        if (!joularEntities.isEmpty()) {
            return new ResponseEntity<>(joularEntities, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
