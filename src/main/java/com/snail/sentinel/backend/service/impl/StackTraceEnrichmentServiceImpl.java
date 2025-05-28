package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.repository.AstElemEntityRepository;
import com.snail.sentinel.backend.service.StackTraceEnrichmentService;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;

import java.util.List;
import java.util.Optional;

public class StackTraceEnrichmentServiceImpl implements StackTraceEnrichmentService {
    private final AstElemEntityRepository astElemEntityRepository;

    public StackTraceEnrichmentServiceImpl(AstElemEntityRepository astElemEntityRepository) {
        this.astElemEntityRepository = astElemEntityRepository;
    }

    @Override
    public List<StackTraceElementDTO> enrichStackTrace(List<StackTraceElement> elements) {
        return elements.stream().map(element -> {
            Optional<AstElemEntityDTO> maybeAst = astElemEntityRepository.findMatchingAstElem(
                element.getFileName(),
                element.getClassName(),
                element.getMethodName(),
                element.getLineNumber()
            );

            List<String> params = maybeAst.map(AstElemEntityDTO::getParameters).orElse(List.of());

            return new StackTraceElementDTO(
                element.getFileName(),
                element.getClassName(),
                element.getMethodName(),
                String.valueOf(element.getLineNumber()),
                params
            );
        }).toList();
    }
}
