package com.snail.sentinel.backend.service.dto.commit;

import jakarta.validation.constraints.NotNull;

public class StatsDTO {
    @NotNull
    private Integer additions;

    @NotNull
    private Integer deletions;

    public Integer getAdditions() {
        return additions;
    }

    public void setAdditions(Integer additions) {
        this.additions = additions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    @Override
    public String toString() {
        return "StatsDTO{" +
            "additions=" + additions +
            ", deletions=" + deletions +
            '}';
    }
}
