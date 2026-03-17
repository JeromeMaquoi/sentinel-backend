package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.service.ConstructorContextEntityService;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.exceptions.ConstructorContextNotCompleteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ConstructorContextResourceTest {
    @Mock
    private ConstructorContextEntityService service;
    @InjectMocks
    private ConstructorContextResource resource;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resource.setApplicationName("sentinelBackendApp");
        mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateConstructorContextTest() throws Exception {
        ConstructorContextDTO dto = new ConstructorContextDTO(
            "MyClass.java",
            "com.example.MyClass",
            "myMethod",
            List.of("int", "String"),
            List.of(),
            List.of(new StackTraceElement("com.example.MyClass", "myMethod", "MyClass.java", 42)),
            "snapshot"
        );

        ConstructorContextEntityDTO responseDTO = new ConstructorContextEntityDTO(
            "MyClass.java",
            "com.example.MyClass",
            "myMethod",
            List.of("int", "String"),
            List.of(),
            List.of(),
            "snapshot"
        );
        responseDTO.setId("123");

        when(service.save(any(ConstructorContextDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v2/constructor-contexts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("X-sentinelBackendApp-alert", "A new constructorContext is created with identifier 123"))
            .andExpect(header().string("X-sentinelBackendApp-params", "123"))
            .andExpect(jsonPath("$.id").value("123"))
            .andExpect(jsonPath("$.fileName").value("MyClass.java"));

        verify(service).save(any(ConstructorContextDTO.class));
    }

    @Test
    void shouldThrowExceptionWhenIncompleteDtoTest() throws Exception {
        ConstructorContextDTO dto = new ConstructorContextDTO(); // incomplete DTO
        assertThrows(ConstructorContextNotCompleteException.class, () -> {
            resource.createConstructorContext(dto);
        });
    }

    @Test
    void shouldCreateConstructorContextsBatchTest() throws Exception {
        List<ConstructorContextDTO> dtos = List.of(
            new ConstructorContextDTO(
                "MyClass1.java",
                "com.example.MyClass1",
                "myMethod1",
                List.of("int"),
                List.of(),
                List.of(new StackTraceElement("com.example.MyClass1", "myMethod1", "MyClass1.java", 10)),
                "snapshot1"
            ),
            new ConstructorContextDTO(
                "MyClass2.java",
                "com.example.MyClass2",
                "myMethod2",
                List.of("String"),
                List.of(),
                List.of(new StackTraceElement("com.example.MyClass2", "myMethod2", "MyClass2.java", 20)),
                "snapshot2"
        ));

        mockMvc.perform(post("/api/v2/constructor-contexts/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtos)))
            .andExpect(status().isCreated())
            .andExpect(header().string("X-sentinelBackendApp-alert", "A new constructorContext is created with identifier batch"));

        verify(service).saveBatch(dtos);
    }

    @Test
    void shouldCreateEmptyConstructorContextsBatchTest()  throws Exception {
        List<ConstructorContextDTO> dtos = List.of();

        mockMvc.perform(post("/api/v2/constructor-contexts/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtos)))
            .andExpect(status().isCreated())
            .andExpect(header().string("X-sentinelBackendApp-alert", "A new constructorContext is created with identifier batch"));

        verify(service).saveBatch(dtos);
    }

    @Test
    void shouldThrowExceptionForIncompleteDtoInBatchTest() {
        List<ConstructorContextDTO> dtos = List.of(new ConstructorContextDTO());

        doThrow(ConstructorContextNotCompleteException.class).when(service).saveBatch(dtos);

        assertThrows(ConstructorContextNotCompleteException.class, () -> {
            resource.createConstructorContextsBatch(dtos);
        });

        verify(service).saveBatch(dtos);
    }
}
