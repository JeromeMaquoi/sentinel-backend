package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.MatchedConstructorDTO;

import java.util.List;

public class AggregatedRuntimeCallTreeWithMatchedConstructorsDTO extends AggregatedRuntimeCallTreeMeasurementDTO {
    private List<MatchedConstructorDTO> matchedConstructors;

    public AggregatedRuntimeCallTreeWithMatchedConstructorsDTO() {
    }

    public AggregatedRuntimeCallTreeWithMatchedConstructorsDTO(List<MatchedConstructorDTO> matchedConstructors) {
        super();
        this.matchedConstructors = matchedConstructors;
    }

    public List<MatchedConstructorDTO> getMatchedConstructors() {
        return matchedConstructors;
    }

    public void setMatchedConstructors(List<MatchedConstructorDTO> matchedConstructors) {
        this.matchedConstructors = matchedConstructors;
    }

    @Override
    public String toString() {
        return "AggregatedRuntimeCallTreeWithConstructorsDTO{" +
            "matchedConstructors=" + matchedConstructors +
            '}';
    }
}
