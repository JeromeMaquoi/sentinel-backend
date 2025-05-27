package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.service.AstElemEntityService;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import com.snail.sentinel.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/ast-elem-entities")
public class AstElemEntityResource {
    private final Logger log = LoggerFactory.getLogger(AstElemEntityResource.class);
    private static final String ENTITY_NAME = "astElemEntity";
    @Value("sentinelBackendApp")
    private String applicationName;

    private final AstElemEntityService astElemEntityService;

    public AstElemEntityResource(AstElemEntityService astElemEntityService) {
        this.astElemEntityService = astElemEntityService;
    }

    @PostMapping("")
    public ResponseEntity<AstElemEntityDTO> createAstElemEntity(@RequestBody AstElemEntityDTO astElemEntityDTO) throws URISyntaxException {
        log.debug("REST request to save AstElemEntity : {}", astElemEntityDTO);
        if (astElemEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new astElemEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AstElemEntityDTO result = astElemEntityService.save(astElemEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/ast-elem-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AstElemEntityDTO>> bulkAddAstElemEntities(@RequestBody List<AstElemEntityDTO> astElemEntityDTOList) {
        log.debug("REST request to bulkAddAstElemEntities : {}", astElemEntityDTOList);
        List<AstElemEntityDTO> result = astElemEntityService.bulkAdd(astElemEntityDTOList);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAllAstElemEntities() {
        log.debug("REST request to delete all AstElemEntities");
        astElemEntityService.deleteAll();
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, "all")).build();
    }
}
