package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.JoularEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/joular")
public class JoularEntityResource {
    private final Logger log = LoggerFactory.getLogger(JoularEntityResource.class);
    private final JoularEntityRepository joularEntityRepository;
    private final JoularEntityService joularEntityService;
    private static final String ENTITY_NAME = "joularEntity";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public JoularEntityResource(JoularEntityRepository joularEntityRepository, JoularEntityService joularEntityService) {
        this.joularEntityRepository = joularEntityRepository;
        this.joularEntityService = joularEntityService;
    }

    @GetMapping("/entities")
    public Page<JoularEntity> getAllJoularEntities(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all JoularEntities");
        PageRequest pageRequest = PageRequest.of(page, size);
        return joularEntityRepository.findAll(pageRequest);
    }

    @GetMapping("/entities/by-commit/{sha}")
    public ResponseEntity<Page<JoularEntity>> getAllJoularDataFromOneCommit(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all joular data from commit : {}", sha);
        List<JoularEntity> joularEntities = joularEntityService.findByCommitSha(sha);
        return getPageResponseEntity(page, size, joularEntities);
    }

    @GetMapping("/entities/by-commit-and-ast-elem/{sha}")
    public ResponseEntity<Page<JoularEntity>> getAllJoularDataFromOneCommitAndAstElem(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size,
        @RequestParam String className,
        @RequestParam String classMethodSignature
    ) {
        log.debug("REST request to get all joular entities data from commit {} and astElem {}", sha, classMethodSignature);
        List<JoularEntity> joularEntities = joularEntityService.findByCommitShaAndAstElement(sha, className, classMethodSignature);
        return getPageResponseEntity(page, size, joularEntities);
    }

    @DeleteMapping("/entities/by-repository/{repoName}")
    public ResponseEntity<Void> deleteJoularEntitiesFromOneRepository(@PathVariable String repoName) {
        log.debug("REST request to delete all joular entities from repository {}", repoName);
        joularEntityRepository.deleteJoularEntitiesByCommit_Repository_Name(repoName);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, repoName)).build();
    }

    private ResponseEntity<Page<JoularEntity>> getPageResponseEntity(int page, int size, List<JoularEntity> joularEntities) {
        if (!joularEntities.isEmpty()) {
            int totalSize = joularEntities.size();
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalSize);

            List<JoularEntity> ckContent = joularEntities.subList(startIndex, endIndex);
            Page<JoularEntity> pageContent = new PageImpl<>(ckContent, PageRequest.of(page, size), totalSize);
            return new ResponseEntity<>(pageContent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
