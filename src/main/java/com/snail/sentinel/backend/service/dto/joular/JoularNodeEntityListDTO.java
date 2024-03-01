package com.snail.sentinel.backend.service.dto.joular;

import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JoularNodeEntityListDTO {
    private final Logger log = LoggerFactory.getLogger(JoularNodeEntityListDTO.class);

    List<JoularNodeEntityDTO> joularNodeEntityDTOList;

    public JoularNodeEntityListDTO() {
        this.joularNodeEntityDTOList = new ArrayList<>();
    }

    public void add(JoularNodeEntityDTO joularNodeEntityDTO) {
        this.joularNodeEntityDTOList.add(joularNodeEntityDTO);
    }

    public List<JoularNodeEntityDTO> getList() {
        return this.joularNodeEntityDTOList;
    }

    public JoularNodeEntityDTO get(int index) {
        return this.joularNodeEntityDTOList.get(index);
    }

    public int size() {
        return this.joularNodeEntityDTOList.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularNodeEntityListDTO that = (JoularNodeEntityListDTO) o;
        return Objects.equals(joularNodeEntityDTOList, that.joularNodeEntityDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joularNodeEntityDTOList);
    }

    @Override
    public String toString() {
        return "JoularNodeEntityListDTO{" +
            "joularNodeEntityDTOList=" + joularNodeEntityDTOList +
            '}';
    }
}
