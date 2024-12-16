package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.impl.ConstructorEntityServiceImpl;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConstructorEntityServiceImplTest {
    @Mock
    private ConstructorEntityRepository constructorEntityRepository;

    @Mock
    private ConstructorEntityMapper constructorEntityMapper;

    @InjectMocks
    private ConstructorEntityServiceImpl constructorEntityService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void saveTest() {
        // Arrange
        ConstructorEntityDTO constructorEntityDTO = new ConstructorEntityDTO();
        ConstructorEntity constructorEntity = new ConstructorEntity();
        when(constructorEntityMapper.toEntity(any(ConstructorEntityDTO.class))).thenReturn(constructorEntity);
        when(constructorEntityRepository.save(any(ConstructorEntity.class))).thenReturn(constructorEntity);
        when(constructorEntityMapper.toDto(any(ConstructorEntity.class))).thenReturn(constructorEntityDTO);

        // Act
        ConstructorEntityDTO result = constructorEntityService.save(constructorEntityDTO);

        // Assert
        assertNotNull(result);
        verify(constructorEntityRepository, times(1)).save(constructorEntity);
    }

    @Test
    void updateTest() {
        // Arrange
        ConstructorEntityDTO constructorEntityDTO = new ConstructorEntityDTO();
        ConstructorEntity constructorEntity = new ConstructorEntity();
        when(constructorEntityMapper.toEntity(any(ConstructorEntityDTO.class))).thenReturn(constructorEntity);
        when(constructorEntityRepository.save(any(ConstructorEntity.class))).thenReturn(constructorEntity);
        when(constructorEntityMapper.toDto(any(ConstructorEntity.class))).thenReturn(constructorEntityDTO);

        // Act
        ConstructorEntityDTO result = constructorEntityService.update(constructorEntityDTO);

        // Assert
        assertNotNull(result);
        verify(constructorEntityRepository, times(1)).save(constructorEntity);
    }

    @Test
    void getOrCreateConstructorWithNewConstructorTest() {
        String signature = "signature1()";
        String name = "ConstructorA";
        String fileName = "fileA.java";
        String className = "ClassA";

        when(constructorEntityRepository.findBySignature(signature)).thenReturn(Optional.empty());
        when(constructorEntityRepository.save(any(ConstructorEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConstructorEntity result = constructorEntityService.getOrCreateConstructor(signature, name, fileName, className);

        assertEquals(signature, result.getSignature());
        assertEquals(name, result.getName());
        assertEquals(fileName, result.getFileName());
        assertEquals(className, result.getClassName());
    }

    @Test
    void findAllTest() {
        // Arrange
        ConstructorEntity constructorEntity = new ConstructorEntity();
        List<ConstructorEntity> constructorEntityList = new LinkedList<>();
        constructorEntityList.add(constructorEntity);
        when(constructorEntityRepository.findAll()).thenReturn(constructorEntityList);
        when(constructorEntityMapper.toDto(any(ConstructorEntity.class))).thenReturn(new ConstructorEntityDTO());

        // Act
        List<ConstructorEntityDTO> result = constructorEntityService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(constructorEntityRepository, times(1)).findAll();
    }

    @Test
    void findOneTest() {
        // Arrange
        ConstructorEntity constructorEntity = new ConstructorEntity();
        ConstructorEntityDTO constructorEntityDTO = new ConstructorEntityDTO();
        when(constructorEntityRepository.findById(anyString())).thenReturn(Optional.of(constructorEntity));
        when(constructorEntityMapper.toDto(any(ConstructorEntity.class))).thenReturn(constructorEntityDTO);

        // Act
        Optional<ConstructorEntityDTO> result = constructorEntityService.findOne("1");

        // Assert
        assertTrue(result.isPresent());
        verify(constructorEntityRepository, times(1)).findById("1");
    }

    @Test
    void deleteTest() {
        // Act
        constructorEntityService.delete("1");

        // Assert
        verify(constructorEntityRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteAllTest() {
        // Act
        constructorEntityService.deleteAll();

        // Assert
        verify(constructorEntityRepository, times(1)).deleteAll();
    }

    @Test
    void getOrCreateConstructorWithExistingConstructorTest() {
        String signature = "TestSignature";
        ConstructorEntity existingEntity = new ConstructorEntity();
        existingEntity.setSignature(signature);

        when(constructorEntityRepository.findBySignature(signature)).thenReturn(Optional.of(existingEntity));

        ConstructorEntity result = constructorEntityService.getOrCreateConstructor(signature, "Name", "File", "Class");

        assertEquals(existingEntity, result);
    }
}
