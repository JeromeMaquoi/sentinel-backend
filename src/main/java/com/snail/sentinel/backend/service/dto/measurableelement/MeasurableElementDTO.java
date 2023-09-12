package com.snail.sentinel.backend.service.dto.measurableelement;

public interface MeasurableElementDTO {

    public abstract String getAstElem();

    public abstract void setAstElem(String astElem);

    public abstract String getFilePath();

    public abstract void setFilePath(String filePath);

    public abstract String getClassName();

    public abstract void setClassName(String className);
}
