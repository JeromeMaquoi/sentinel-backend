package com.snail.sentinel.backend.service.dto.joular;

import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class JoularAggregateDTO {
    @NotNull
    private List<Float> allValues;

    @NotNull
    private CommitSimpleDTO commit;

    @NotNull
    private MeasurableElementDTO measurableElement;

    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Float> getAllValues() {
        return allValues;
    }

    public void setAllValues(List<Float> allValues) {
        this.allValues = allValues;
    }

    public CommitSimpleDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitSimpleDTO commit) {
        this.commit = commit;
    }

    public MeasurableElementDTO getMeasurableElement() {
        return measurableElement;
    }

    public void setMeasurableElement(MeasurableElementDTO measurableElement) {
        this.measurableElement = measurableElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularAggregateDTO that = (JoularAggregateDTO) o;
        return Objects.equals(allValues, that.allValues) && Objects.equals(commit, that.commit) && Objects.equals(measurableElement, that.measurableElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allValues, commit, measurableElement);
    }

    @Override
    public String toString() {
        return "JoularAggregateDTO{" +
            "allValues=" + allValues +
            ", commit=" + commit +
            ", measurableElement=" + measurableElement +
            '}';
    }
}
