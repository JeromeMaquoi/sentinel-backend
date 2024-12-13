package com.snail.sentinel.backend.service.dto;

public class RegisterAttributeRequest {
    private String constructorSignature;
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
}
