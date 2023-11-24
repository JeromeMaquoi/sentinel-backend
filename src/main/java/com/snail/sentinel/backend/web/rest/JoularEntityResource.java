package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class JoularEntityResource {
    private final Logger log = LoggerFactory.getLogger(JoularEntityResource.class);
    private final JoularEntityRepository joularEntityRepository;

    public JoularEntityResource(JoularEntityRepository joularEntityRepository) {
        this.joularEntityRepository = joularEntityRepository;
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
}
