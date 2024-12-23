package com.snail.sentinel.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorEntityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConstructorEntityAllPropertiesEquals(ConstructorEntity expected, ConstructorEntity actual) {
        assertConstructorEntityAutoGeneratedPropertiesEquals(expected, actual);
        assertConstructorEntityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConstructorEntityAllUpdatablePropertiesEquals(ConstructorEntity expected, ConstructorEntity actual) {
        assertConstructorEntityUpdatableFieldsEquals(expected, actual);
        assertConstructorEntityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConstructorEntityAutoGeneratedPropertiesEquals(ConstructorEntity expected, ConstructorEntity actual) {
        assertThat(expected)
            .as("Verify ConstructorEntity auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConstructorEntityUpdatableFieldsEquals(ConstructorEntity expected, ConstructorEntity actual) {
        assertThat(expected)
            .as("Verify ConstructorEntity relevant properties")
            .satisfies(e -> assertThat(e.getSignature()).as("check signature").isEqualTo(actual.getSignature()))
            .satisfies(e -> assertThat(e.getClassName()).as("check className").isEqualTo(actual.getClassName()))
            .satisfies(e -> assertThat(e.getFileName()).as("check file").isEqualTo(actual.getFileName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConstructorEntityUpdatableRelationshipsEquals(ConstructorEntity expected, ConstructorEntity actual) {
        // empty method
    }
}
