package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;

import java.util.List;

public interface StackTraceEnrichmentService {
    List<StackTraceElementDTO> enrichStackTrace(List<StackTraceElement> elements);
}
