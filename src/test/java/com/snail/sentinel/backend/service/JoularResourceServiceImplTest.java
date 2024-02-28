package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.impl.JoularResourceServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JoularResourceServiceImplTest {
    @Mock
    private CommitEntityService commitEntityService;
    @Mock
    private CkEntityRepositoryAggregation ckEntityRepositoryAggregation;
    @InjectMocks
    private JoularResourceServiceImpl joularResourceService;

    @BeforeEach
    public void init() {
        commitEntityService = Mockito.mock(CommitEntityService.class);
        ckEntityRepositoryAggregation = Mockito.mock(CkEntityRepositoryAggregation.class);
        joularResourceService = new JoularResourceServiceImpl(commitEntityService, ckEntityRepositoryAggregation);
    }

    @Test
    void simpleGetClassMethodLineTest() {
        String line = "org.apache.commons.configuration2.TestINIConfiguration.testValueWithSemicolon 1006";
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine(line);
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.get();

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.apache.commons.configuration2.TestINIConfiguration");
        classMethodLine.put("methodName", "testValueWithSemicolon");
        classMethodLine.put("lineNumber", 1006);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }

    @Test
    void simple2GetClassMethodLineTest() {
        Optional<JSONObject> optionalResult = joularResourceService.getClassMethodLine("org.springframework.boot.context.config.ConfigDataLocationRuntimeHints.getFileNames 60");
        assertTrue(optionalResult.isPresent());
        JSONObject maybeClassMethodLine = optionalResult.get();

        JSONObject classMethodLine = new JSONObject();
        classMethodLine.put("className", "org.springframework.boot.context.config.ConfigDataLocationRuntimeHints");
        classMethodLine.put("methodName", "getFileNames");
        classMethodLine.put("lineNumber", 60);

        assertTrue(maybeClassMethodLine.similar(classMethodLine));
    }
}
