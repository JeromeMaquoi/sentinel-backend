package com.snail.sentinel.backend.web.rest;

import static com.snail.sentinel.backend.domain.ConstructorEntityAsserts.*;
//import static com.snail.sentinel.backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

/**
 * Integration tests for the {@link ConstructorEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConstructorEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATURE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE = "BBBBBBBBBB";

    private static final String DEFAULT_PKG = "AAAAAAAAAA";
    private static final String UPDATED_PKG = "BBBBBBBBBB";

    private static final String DEFAULT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_FILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v1/constructor-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConstructorEntityRepository constructorEntityRepository;

    @Autowired
    private ConstructorEntityMapper constructorEntityMapper;

    @Autowired
    private MockMvc restConstructorEntityMockMvc;

    private ConstructorEntity constructorEntity;

    private ConstructorEntity insertedConstructorEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConstructorEntity createEntity() {
        return new ConstructorEntity().name(DEFAULT_NAME).signature(DEFAULT_SIGNATURE).className(DEFAULT_PKG).file(DEFAULT_FILE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConstructorEntity createUpdatedEntity() {
        return new ConstructorEntity().name(UPDATED_NAME).signature(UPDATED_SIGNATURE).className(UPDATED_PKG).file(UPDATED_FILE);
    }

    @BeforeEach
    public void initTest() {
        constructorEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedConstructorEntity != null) {
            constructorEntityRepository.delete(insertedConstructorEntity);
            insertedConstructorEntity = null;
        }
    }

    @Test
    void createConstructorEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);
        var returnedConstructorEntityDTO = om.readValue(
            restConstructorEntityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(constructorEntityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConstructorEntityDTO.class
        );

        // Validate the ConstructorEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConstructorEntity = constructorEntityMapper.toEntity(returnedConstructorEntityDTO);
        assertConstructorEntityUpdatableFieldsEquals(returnedConstructorEntity, getPersistedConstructorEntity(returnedConstructorEntity));

        insertedConstructorEntity = returnedConstructorEntity;
    }

    @Test
    void getAllConstructorEntities() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        // Get all the constructorEntityList
        restConstructorEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(constructorEntity.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(DEFAULT_SIGNATURE)))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_PKG)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE)));
    }

    @Test
    void getConstructorEntity() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        // Get the constructorEntity
        restConstructorEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, constructorEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(constructorEntity.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.signature").value(DEFAULT_SIGNATURE))
            .andExpect(jsonPath("$.className").value(DEFAULT_PKG))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE));
    }

    @Test
    void getNonExistingConstructorEntity() throws Exception {
        // Get the constructorEntity
        restConstructorEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingConstructorEntity() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the constructorEntity
        ConstructorEntity updatedConstructorEntity = constructorEntityRepository.findById(constructorEntity.getId()).orElseThrow();
        updatedConstructorEntity.name(UPDATED_NAME).signature(UPDATED_SIGNATURE).className(UPDATED_PKG).file(UPDATED_FILE);
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(updatedConstructorEntity);

        restConstructorEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, constructorEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(constructorEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConstructorEntityToMatchAllProperties(updatedConstructorEntity);
    }

    @Test
    void putNonExistingConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, constructorEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(constructorEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(constructorEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(constructorEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    /*@Test
    void partialUpdateConstructorEntityWithPatch() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the constructorEntity using partial update
        ConstructorEntity partialUpdatedConstructorEntity = new ConstructorEntity();
        partialUpdatedConstructorEntity.setId(constructorEntity.getId());

        partialUpdatedConstructorEntity.name(UPDATED_NAME);

        restConstructorEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstructorEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConstructorEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConstructorEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConstructorEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConstructorEntity, constructorEntity),
            getPersistedConstructorEntity(constructorEntity)
        );
    }*/

    @Test
    void fullUpdateConstructorEntityWithPatch() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the constructorEntity using partial update
        ConstructorEntity partialUpdatedConstructorEntity = new ConstructorEntity();
        partialUpdatedConstructorEntity.setId(constructorEntity.getId());

        partialUpdatedConstructorEntity.name(UPDATED_NAME).signature(UPDATED_SIGNATURE).className(UPDATED_PKG).file(UPDATED_FILE);

        restConstructorEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstructorEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConstructorEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConstructorEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConstructorEntityUpdatableFieldsEquals(
            partialUpdatedConstructorEntity,
            getPersistedConstructorEntity(partialUpdatedConstructorEntity)
        );
    }

    @Test
    void patchNonExistingConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, constructorEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(constructorEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(constructorEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamConstructorEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        constructorEntity.setId(UUID.randomUUID().toString());

        // Create the ConstructorEntity
        ConstructorEntityDTO constructorEntityDTO = constructorEntityMapper.toDto(constructorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstructorEntityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(constructorEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConstructorEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteConstructorEntity() throws Exception {
        // Initialize the database
        insertedConstructorEntity = constructorEntityRepository.save(constructorEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the constructorEntity
        restConstructorEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, constructorEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return constructorEntityRepository.count();
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

    protected ConstructorEntity getPersistedConstructorEntity(ConstructorEntity constructorEntity) {
        return constructorEntityRepository.findById(constructorEntity.getId()).orElseThrow();
    }

    protected void assertPersistedConstructorEntityToMatchAllProperties(ConstructorEntity expectedConstructorEntity) {
        assertConstructorEntityAllPropertiesEquals(expectedConstructorEntity, getPersistedConstructorEntity(expectedConstructorEntity));
    }

    protected void assertPersistedConstructorEntityToMatchUpdatableProperties(ConstructorEntity expectedConstructorEntity) {
        assertConstructorEntityAllUpdatablePropertiesEquals(
            expectedConstructorEntity,
            getPersistedConstructorEntity(expectedConstructorEntity)
        );
    }
}
