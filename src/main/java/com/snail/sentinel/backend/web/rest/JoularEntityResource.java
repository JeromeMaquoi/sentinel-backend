package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.JoularEntityService;
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
@RequestMapping("/api/v1")
public class JoularEntityResource {
    private final Logger log = LoggerFactory.getLogger(JoularEntityResource.class);
    private final JoularEntityRepository joularEntityRepository;
    private final JoularEntityService joularService;

    public JoularEntityResource(JoularEntityRepository joularEntityRepository, JoularEntityService joularService) {
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
    public ResponseEntity<Page<JoularEntity>> getAllJoularDataFromOneCommit(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size
    ) {
        log.debug("REST request to get all joular data from commit : {}", sha);
        List<JoularEntity> joularEntities = joularService.findByCommitSha(sha);
        return getPageResponseEntity(page, size, joularEntities);
    }

    @GetMapping("/joular-entities/by-commit-and-ast-elem/{sha}")
    public ResponseEntity<Page<JoularEntity>> getAllJoularDataFromOneCommitAndAstElem(
        @PathVariable String sha,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "100") int size,
        @RequestParam String className,
        @RequestParam String methodSignature
    ) {
        log.debug("REST request to get all joular entities data from commit {} and astElem {}", sha, className + "." + methodSignature);
        List<JoularEntity> joularEntities = joularService.findByCommitShaAndAstElement(sha, className, methodSignature);
        return getPageResponseEntity(page, size, joularEntities);

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
