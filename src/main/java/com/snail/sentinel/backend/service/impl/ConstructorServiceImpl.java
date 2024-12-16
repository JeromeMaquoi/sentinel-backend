package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.ConstructorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstructorServiceImpl implements ConstructorService {
    private final ConstructorEntityRepository constructorEntityRepository;

    public ConstructorServiceImpl(ConstructorEntityRepository constructorEntityRepository) {
        this.constructorEntityRepository = constructorEntityRepository;
    }

    @Override
    @Transactional
    public ConstructorEntity getOrCreateConstructor(String signature, String name, String fileName, String className) {
        return constructorEntityRepository.findBySignature(signature).orElseGet(() -> {
            ConstructorEntity newConstructorEntity = new ConstructorEntity();
            newConstructorEntity.setSignature(signature);
            newConstructorEntity.setName(name);
            newConstructorEntity.setFileName(fileName);
            newConstructorEntity.setClassName(className);
            return constructorEntityRepository.save(newConstructorEntity);
        });
    }
}
