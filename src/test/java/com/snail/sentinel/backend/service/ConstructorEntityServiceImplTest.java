package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.impl.ConstructorEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ConstructorEntityServiceImplTest {
    @Mock
    private ConstructorEntityRepository constructorEntityRepository;

    @InjectMocks
    private ConstructorEntityServiceImpl constructorService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getOrCreateConstructorWithNewConstructorTest() {
        String signature = "signature1()";
        String name = "ConstructorA";
        String fileName = "fileA.java";
        String className = "ClassA";

        when(constructorEntityRepository.findBySignature(signature)).thenReturn(Optional.empty());
        when(constructorEntityRepository.save(any(ConstructorEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConstructorEntity result = constructorService.getOrCreateConstructor(signature, name, fileName, className);

        assertEquals(signature, result.getSignature());
        assertEquals(name, result.getName());
        assertEquals(fileName, result.getFileName());
        assertEquals(className, result.getClassName());
    }

    @Test
    void getOrCreateConstructorWithExistingConstructorTest() {
        String signature = "TestSignature";
        ConstructorEntity existingEntity = new ConstructorEntity();
        existingEntity.setSignature(signature);

        when(constructorEntityRepository.findBySignature(signature)).thenReturn(Optional.of(existingEntity));

        ConstructorEntity result = constructorService.getOrCreateConstructor(signature, "Name", "File", "Class");

        assertEquals(existingEntity, result);
    }
}
