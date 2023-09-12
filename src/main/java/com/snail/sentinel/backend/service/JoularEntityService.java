package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;

import java.util.List;

public interface JoularEntityService {
    List<JoularEntityDTO> findAll();

    List<JoularEntityDTO> bulkAdd(List<JoularEntityDTO> listJoular);

    void deleteAll();
}
