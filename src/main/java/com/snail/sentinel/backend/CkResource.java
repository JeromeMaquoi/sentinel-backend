package com.snail.sentinel.backend;

import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CkResource {

    private String toolVersion = "ck-0.7.0-jar-with-dependencies";
    private List<String> astElem = Arrays.asList("method", "class", "variable");
    public ResponseEntity<CkEntityDTO> createCkEntity() {

    }
}
