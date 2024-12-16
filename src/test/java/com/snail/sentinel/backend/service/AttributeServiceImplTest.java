package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
import com.snail.sentinel.backend.service.impl.AttributeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttributeServiceImplTest {
    @Mock
    private AttributeEntityRepository attributeEntityRepository;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @BeforeEach
    public void init() {
        attributeEntityRepository = mock(AttributeEntityRepository.class);
        attributeService = new AttributeServiceImpl(attributeEntityRepository);
    }

    @Test
    void attributeExistsTest() {
        ConstructorEntity constructorEntity = new ConstructorEntity();
        AttributeEntity existingAttributeEntity = new AttributeEntity();
        existingAttributeEntity.setName("name");
        existingAttributeEntity.setType("String");

        constructorEntity.setAttributeEntities(Collections.singleton(existingAttributeEntity));

        boolean result = attributeService.attributeExists(constructorEntity, "name", "String");
        assertTrue(result);
    }

    @Test
    void createAttributeTest() {
        String attributeName = "name";
        String attributeType = "String";
        when(attributeEntityRepository.save(any(AttributeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttributeEntity result = attributeService.createAttribute(attributeName, attributeType);

        assertEquals(attributeName, result.getName());
        assertEquals(attributeType, result.getType());
    }
}
