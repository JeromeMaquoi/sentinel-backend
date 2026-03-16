package com.snail.sentinel.backend.service.impl;

import com.mongodb.bulk.BulkWriteError;
import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.exceptions.ConstructorContextEntityExistsException;
import com.snail.sentinel.backend.service.mapper.ConstructorContextEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConstructorContextEntityServiceImplTest {
    @Mock
    private ConstructorContextEntityRepository repository;

    @Mock
    private ConstructorContextEntityMapper mapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private BulkOperations bulkOperations;

    @InjectMocks
    private ConstructorContextEntityServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveShouldPersisEntityWhenNoDuplicateExistsTest() {
        ConstructorContextDTO dto = mock(ConstructorContextDTO.class);
        ConstructorContextEntityDTO entityDTO =  mock(ConstructorContextEntityDTO.class);
        ConstructorContextEntity savedEntity = mock(ConstructorContextEntity.class);

        when(dto.getFileName()).thenReturn("file");
        when(dto.getClassName()).thenReturn("clazz");
        when(dto.getMethodName()).thenReturn("method");
        when(dto.getParameters()).thenReturn(List.of("params"));

        when(repository.findByFileNameAndClassNameAndMethodNameAndParameters(any(), any(), any(), any())).thenReturn(List.of());

        when(mapper.toDto(dto)).thenReturn(entityDTO);
        when(mapper.toEntity(entityDTO)).thenReturn(savedEntity);
        when(repository.save(savedEntity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(entityDTO);

        ConstructorContextEntityDTO result = service.save(dto);
        assertEquals(entityDTO, result);
        verify(repository).save(savedEntity);
    }

    @Test
    void saveShouldThrowExceptionWhenDuplicateExistsTest() {
        ConstructorContextDTO dto = mock(ConstructorContextDTO.class);
        ConstructorContextEntityDTO existingDTO =  mock(ConstructorContextEntityDTO.class);
        ConstructorContextEntityDTO newDTO = mock(ConstructorContextEntityDTO.class);

        when(dto.getFileName()).thenReturn("file");
        when(dto.getClassName()).thenReturn("clazz");
        when(dto.getMethodName()).thenReturn("method");
        when(dto.getParameters()).thenReturn(List.of("params"));

        when(repository.findByFileNameAndClassNameAndMethodNameAndParameters(any(), any(), any(), any())).thenReturn(List.of(existingDTO));
        when(mapper.toDto(dto)).thenReturn(newDTO);

        StackTraceElementDTO trace = new StackTraceElementDTO("file", "class", "methodTrace", 10);
        when(existingDTO.getStacktrace()).thenReturn(List.of(trace));
        when(newDTO.getStacktrace()).thenReturn(List.of(trace));

        assertThrows(ConstructorContextEntityExistsException.class, () -> service.save(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void saveBatchShouldReturnWhenBatchIsEmptyTest() {
        service.saveBatch(List.of());
        verifyNoInteractions(mongoTemplate);
        service.saveBatch(null);
        verifyNoInteractions(repository);
    }

    @Test
    void saveBatchShouldInsertEntitiesWhenValidBatchTest() {
        ConstructorContextDTO dto = mock(ConstructorContextDTO.class);
        ConstructorContextEntity entity = mock(ConstructorContextEntity.class);

        when(dto.isComplete()).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ConstructorContextEntity.class)).thenReturn(bulkOperations);
        when(bulkOperations.insert(anyList())).thenReturn(bulkOperations);

        service.saveBatch(List.of(dto));

        verify(entity).computeStacktraceHash();
        verify(bulkOperations).insert(anyList());
        verify(bulkOperations).execute();
    }

    @Test
    void saveBatchShouldIgnoreDuplicateErrorsTest() {
        ConstructorContextDTO dto = mock(ConstructorContextDTO.class);
        ConstructorContextEntity entity = mock(ConstructorContextEntity.class);
        when(dto.isComplete()).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);

        when(mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ConstructorContextEntity.class)).thenReturn(bulkOperations);
        when(bulkOperations.insert(anyList())).thenReturn(bulkOperations);

        BulkOperationException exception = mock(BulkOperationException.class);
        BulkWriteError error =  mock(BulkWriteError.class);

        when(error.getCode()).thenReturn(11000);
        when(exception.getErrors()).thenReturn(List.of(error));
        doThrow(exception).when(bulkOperations).execute();

        service.saveBatch(List.of(dto));

        verify(bulkOperations).execute();
    }

    @Test
    void saveBatchShouldRethrowUnexpectedExceptionTest() {
        ConstructorContextDTO dto = mock(ConstructorContextDTO.class);
        ConstructorContextEntity entity = mock(ConstructorContextEntity.class);

        when(dto.isComplete()).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);

        when(mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ConstructorContextEntity.class)).thenReturn(bulkOperations);
        when(bulkOperations.insert(anyList())).thenReturn(bulkOperations);

        RuntimeException exception = new  RuntimeException("unexpected");
        doThrow(exception).when(bulkOperations).execute();

        assertThatThrownBy(() ->  service.saveBatch(List.of(dto))).isInstanceOf(RuntimeException.class).hasMessageContaining("unexpected");
    }
}
