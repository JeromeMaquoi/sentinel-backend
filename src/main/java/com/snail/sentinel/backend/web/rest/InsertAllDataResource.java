package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.CkJoularResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InsertAllDataResource controller
 */
@RestController
@RequestMapping("/api/insert-all-data")
public class InsertAllDataResource {

    private final Logger log = LoggerFactory.getLogger(InsertAllDataResource.class);

    @Autowired
    private CkJoularResource ckJoularResource;

    /**
     * GET defaultAction
     */
    @GetMapping("")
    public void insertAllData() {
        log.info("API request to insert all data to the DB");
        try {
            ckJoularResource.insertAllData();
            log.info("All data inserted to the database !");
        } catch (Exception e) {
            log.error("Error during the insertion of the data: {}", e.getMessage());
        }
    }
}
