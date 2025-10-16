package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.ConstructorContextEntityService;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.exceptions.ConstructorContextNotCompleteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v2/constructor-contexts")
public class ConstructorContextResource {
    private final Logger log = LoggerFactory.getLogger(ConstructorContextResource.class);
    private static final String ENTITY_NAME = "constructorContext";

    @Value("sentinelBackendApp")
    private String applicationName;

    private final ConstructorContextEntityRepository repository;

    private final ConstructorContextEntityService service;

    public ConstructorContextResource(ConstructorContextEntityRepository repository, ConstructorContextEntityService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<ConstructorContextEntityDTO> createConstructorContext(@RequestBody ConstructorContextDTO constructorContextDTO) throws URISyntaxException {
        log.debug("REST request to save ConstructorContext : {}", constructorContextDTO);
        if (!constructorContextDTO.isComplete()) {
            throw new ConstructorContextNotCompleteException();
        }
        ConstructorContextEntityDTO result = service.save(constructorContextDTO);
        System.out.println("result: " + result);
        return ResponseEntity.created(new URI("/api/v2/constructor-contexts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }
}
