package com.snail.sentinel.backend.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.snail.sentinel.backend.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the InsertAllDataResource REST controller.
 *
 * @see InsertAllDataResource
 */
@IntegrationTest
class InsertAllDataResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        InsertAllDataResource insertAllDataResource = new InsertAllDataResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(insertAllDataResource).build();
    }

    /**
     * Test defaultAction
     */
    @Test
    void testDefaultAction() throws Exception {
        restMockMvc.perform(get("/api/insert-all-data/")).andExpect(status().isOk());
    }
}
