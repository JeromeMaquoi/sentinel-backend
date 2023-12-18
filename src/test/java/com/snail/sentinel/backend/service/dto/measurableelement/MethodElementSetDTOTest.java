package com.snail.sentinel.backend.service.dto.measurableelement;

import static org.assertj.core.api.Assertions.assertThat;

import com.snail.sentinel.backend.commons.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodElementSetDTOTest {
    private static final String FILE_PATH_1 = "/file/path/to/files";
    private static final String FILE_PATH_2 = "/file/path/to/files2";
    private static final String CLASS_NAME_1 = "className";
    private static final String CLASS_NAME_2 = "className2";

    private static MeasurableElementDTO method1 = new MeasurableElementDTO();
    private static MeasurableElementDTO method2 = new MeasurableElementDTO();
    private static MethodElementSetDTO methodSet = new MethodElementSetDTO();

    @BeforeEach
    public void init() {
        method1.setAstElem(Util.AST_ELEM_METHOD);
        method1.setFilePath(FILE_PATH_1);
        method1.setClassName(CLASS_NAME_1);
        method1.setMethodSignature("method1");

        method2.setAstElem(Util.AST_ELEM_METHOD);
        method2.setFilePath(FILE_PATH_2);
        method2.setClassName(CLASS_NAME_2);
        method2.setMethodSignature("method2");

        methodSet.add(method1);
        methodSet.add(method2);
    }

    @Test
    void has() {
        MeasurableElementDTO hasMethod = new MeasurableElementDTO();
        hasMethod.setAstElem(Util.AST_ELEM_METHOD);
        hasMethod.setFilePath(FILE_PATH_1);
        hasMethod.setClassName(CLASS_NAME_1);
        hasMethod.setMethodSignature("method1");

        assertThat(methodSet.has(hasMethod)).isTrue();
    }

    @Test
    void hasNot() {
        MeasurableElementDTO hasMethod = new MeasurableElementDTO();
        hasMethod.setAstElem(Util.AST_ELEM_METHOD);
        hasMethod.setFilePath(FILE_PATH_1);
        hasMethod.setClassName("otherClassName");
        hasMethod.setMethodSignature("method111111");

        assertThat(methodSet.has(hasMethod)).isFalse();
    }
}
