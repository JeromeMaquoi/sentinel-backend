package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.repository.AstElemEntityRepository;
import com.snail.sentinel.backend.service.StackTraceEnrichmentService;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackTraceEnrichmentServiceImpl implements StackTraceEnrichmentService {
    private static final Logger log = LoggerFactory.getLogger(StackTraceEnrichmentServiceImpl.class);

    private final AstElemEntityRepository astElemEntityRepository;

    public StackTraceEnrichmentServiceImpl(AstElemEntityRepository astElemEntityRepository) {
        this.astElemEntityRepository = astElemEntityRepository;
    }

    @Override
    public List<StackTraceElementDTO> enrichStackTrace(List<StackTraceElement> elements) {
        log.debug("Enriching stacktrace");
        return elements.stream().map(element -> {
            Optional<AstElemEntityDTO> maybeAst = astElemEntityRepository.findMatchingAstElem(
                element.getFileName(),
                element.getClassName(),
                element.getMethodName(),
                element.getLineNumber()
            );

            log.info("found AstElemEntityDTO: {}", maybeAst.isPresent());

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
