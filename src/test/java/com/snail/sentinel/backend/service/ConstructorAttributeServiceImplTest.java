package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.dto.RegisterAttributeRequest;
import com.snail.sentinel.backend.service.impl.ConstructorAttributeServiceImpl;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ConstructorAttributeServiceImplTest {
    @Mock
    private ConstructorService constructorService;

    @Mock
    private AttributeEntityRepository attributeEntityRepository;

    @Mock
    private AttributeService attributeService;

    @Mock
    private ConstructorEntityMapper constructorEntityMapper;

    @Mock
    private ConstructorEntityRepository constructorEntityRepository;

    @InjectMocks
    private ConstructorAttributeServiceImpl constructorAttributeService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterAttribute_NewAttribute() {
        RegisterAttributeRequest request = new RegisterAttributeRequest();
        request.setConstructorSignature("TestSignature");
        request.setConstructorName("TestConstructor");
        request.setConstructorFileName("TestFile.java");
        request.setConstructorClassName("TestClass");
        request.setAttributeName("attr");
        request.setAttributeType("String");

        ConstructorEntity constructorEntity = new ConstructorEntity();
        when(constructorService.getOrCreateConstructor(anyString(), anyString(), anyString(), anyString())).thenReturn(constructorEntity);
        when(attributeService.attributeExists(any(ConstructorEntity.class), anyString(), anyString())).thenReturn(false);
        when(attributeService.createAttribute(anyString(), anyString())).thenReturn(new AttributeEntity());
        when(constructorEntityRepository.save(any(ConstructorEntity.class))).thenReturn(constructorEntity);
        when(constructorEntityMapper.toDto(any(ConstructorEntity.class))).thenReturn(new ConstructorEntityDTO());

        ConstructorEntityDTO result = constructorAttributeService.registerAttribute(request);

        assertNotNull(result);
    }
}
