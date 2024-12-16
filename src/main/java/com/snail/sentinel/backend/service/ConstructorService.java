package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.ConstructorEntity;

public interface ConstructorService {
    ConstructorEntity getOrCreateConstructor(String signature, String name, String fileName, String className);
}
