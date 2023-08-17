package com.snail.sentinel.backend.service.dto.measurableelement;

import jakarta.validation.constraints.NotNull;

public class MeasurableElementDTO {
    @NotNull
    private String astElem;

    @NotNull
    private String filePath;

    @NotNull
    private String className;

    public String getAstElem() {
        return astElem;
    }

    public void setAstElem(String astElem) {
        this.astElem = astElem;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
