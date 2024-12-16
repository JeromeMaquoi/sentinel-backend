package com.snail.sentinel.backend.service.dto;

public class RegisterAttributeRequest {
    private String constructorSignature;
    private String constructorName;
    private String constructorClassName;
    private String constructorFileName;
    private String attributeName;
    private String attributeType;

    public String getConstructorSignature() {
        return constructorSignature;
    }

    public void setConstructorSignature(String constructorSignature) {
        this.constructorSignature = constructorSignature;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName = constructorName;
    }

    public String getConstructorClassName() {
        return constructorClassName;
    }

    public void setConstructorClassName(String constructorClassName) {
        this.constructorClassName = constructorClassName;
    }

    public String getConstructorFileName() {
        return constructorFileName;
    }

    public void setConstructorFileName(String constructorFileName) {
        this.constructorFileName = constructorFileName;
    }
}
