package com.snail.sentinel.backend.web.rest;

import static com.snail.sentinel.backend.domain.AttributeEntityAsserts.*;
//import static com.snail.sentinel.backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link AttributeEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attribute-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttributeEntityRepository attributeEntityRepository;

    @Autowired
    private MockMvc restAttributeEntityMockMvc;

    private AttributeEntity attributeEntity;

    private AttributeEntity insertedAttributeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeEntity createEntity() {
        return new AttributeEntity().name(DEFAULT_NAME).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeEntity createUpdatedEntity() {
        return new AttributeEntity().name(UPDATED_NAME).type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        attributeEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttributeEntity != null) {
            attributeEntityRepository.delete(insertedAttributeEntity);
            insertedAttributeEntity = null;
        }
    }

    @Test
    void createAttributeEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AttributeEntity
        var returnedAttributeEntity = om.readValue(
            restAttributeEntityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeEntity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AttributeEntity.class
        );

        // Validate the AttributeEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAttributeEntityUpdatableFieldsEquals(returnedAttributeEntity, getPersistedAttributeEntity(returnedAttributeEntity));

        insertedAttributeEntity = returnedAttributeEntity;
    }

    @Test
    void createAttributeEntityWithExistingId() throws Exception {
        // Create the AttributeEntity with an existing ID
        attributeEntity.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeEntity)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAttributeEntities() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        // Get all the attributeEntityList
        restAttributeEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeEntity.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    void getAttributeEntity() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        // Get the attributeEntity
        restAttributeEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, attributeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeEntity.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingAttributeEntity() throws Exception {
        // Get the attributeEntity
        restAttributeEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAttributeEntity() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeEntity
        AttributeEntity updatedAttributeEntity = attributeEntityRepository.findById(attributeEntity.getId()).orElseThrow();
        updatedAttributeEntity.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAttributeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributeEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAttributeEntity))
            )
            .andExpect(status().isOk());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttributeEntityToMatchAllProperties(updatedAttributeEntity);
    }

    @Test
    void putNonExistingAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    /*@Test
    void partialUpdateAttributeEntityWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeEntity using partial update
        AttributeEntity partialUpdatedAttributeEntity = new AttributeEntity();
        partialUpdatedAttributeEntity.setId(attributeEntity.getId());

        restAttributeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeEntity))
            )
            .andExpect(status().isOk());

        // Validate the AttributeEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttributeEntity, attributeEntity),
            getPersistedAttributeEntity(attributeEntity)
        );
    }*/

    @Test
    void fullUpdateAttributeEntityWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeEntity using partial update
        AttributeEntity partialUpdatedAttributeEntity = new AttributeEntity();
        partialUpdatedAttributeEntity.setId(attributeEntity.getId());

        partialUpdatedAttributeEntity.name(UPDATED_NAME).type(UPDATED_TYPE);

        restAttributeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeEntity))
            )
            .andExpect(status().isOk());

        // Validate the AttributeEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeEntityUpdatableFieldsEquals(
            partialUpdatedAttributeEntity,
            getPersistedAttributeEntity(partialUpdatedAttributeEntity)
        );
    }

    @Test
    void patchNonExistingAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttributeEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeEntityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attributeEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttributeEntity() throws Exception {
        // Initialize the database
        insertedAttributeEntity = attributeEntityRepository.save(attributeEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attributeEntity
        restAttributeEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributeEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attributeEntityRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AttributeEntity getPersistedAttributeEntity(AttributeEntity attributeEntity) {
        return attributeEntityRepository.findById(attributeEntity.getId()).orElseThrow();
    }

    protected void assertPersistedAttributeEntityToMatchAllProperties(AttributeEntity expectedAttributeEntity) {
        assertAttributeEntityAllPropertiesEquals(expectedAttributeEntity, getPersistedAttributeEntity(expectedAttributeEntity));
    }

    protected void assertPersistedAttributeEntityToMatchUpdatableProperties(AttributeEntity expectedAttributeEntity) {
        assertAttributeEntityAllUpdatablePropertiesEquals(expectedAttributeEntity, getPersistedAttributeEntity(expectedAttributeEntity));
    }
}
