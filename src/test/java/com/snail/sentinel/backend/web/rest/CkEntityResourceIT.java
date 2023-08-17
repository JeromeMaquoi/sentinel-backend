package com.snail.sentinel.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CkEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CkEntityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final String DEFAULT_TOOL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_TOOL_VERSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ck-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CkEntityRepository ckEntityRepository;

    @Autowired
    private CkEntityMapper ckEntityMapper;

    @Autowired
    private MockMvc restCkEntityMockMvc;

    private CkEntity ckEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CkEntity createEntity() {
        CkEntity ckEntity = new CkEntity().name(DEFAULT_NAME).value(DEFAULT_VALUE).toolVersion(DEFAULT_TOOL_VERSION);
        return ckEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CkEntity createUpdatedEntity() {
        CkEntity ckEntity = new CkEntity().name(UPDATED_NAME).value(UPDATED_VALUE).toolVersion(UPDATED_TOOL_VERSION);
        return ckEntity;
    }

    @BeforeEach
    public void initTest() {
        ckEntityRepository.deleteAll();
        ckEntity = createEntity();
    }

    @Test
    void createCkEntity() throws Exception {
        int databaseSizeBeforeCreate = ckEntityRepository.findAll().size();
        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);
        restCkEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isCreated());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeCreate + 1);
        CkEntity testCkEntity = ckEntityList.get(ckEntityList.size() - 1);
        assertThat(testCkEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCkEntity.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCkEntity.getToolVersion()).isEqualTo(DEFAULT_TOOL_VERSION);
    }

    @Test
    void createCkEntityWithExistingId() throws Exception {
        // Create the CkEntity with an existing ID
        ckEntity.setId("existing_id");
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        int databaseSizeBeforeCreate = ckEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCkEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ckEntityRepository.findAll().size();
        // set the field null
        ckEntity.setName(null);

        // Create the CkEntity, which fails.
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        restCkEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isBadRequest());

        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ckEntityRepository.findAll().size();
        // set the field null
        ckEntity.setValue(null);

        // Create the CkEntity, which fails.
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        restCkEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isBadRequest());

        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTool_versionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ckEntityRepository.findAll().size();
        // set the field null
        ckEntity.setToolVersion(null);

        // Create the CkEntity, which fails.
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        restCkEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isBadRequest());

        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCkEntities() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        // Get all the ckEntityList
        restCkEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ckEntity.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].tool_version").value(hasItem(DEFAULT_TOOL_VERSION)));
    }

    @Test
    void getCkEntity() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        // Get the ckEntity
        restCkEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, ckEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ckEntity.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.tool_version").value(DEFAULT_TOOL_VERSION));
    }

    @Test
    void getNonExistingCkEntity() throws Exception {
        // Get the ckEntity
        restCkEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCkEntity() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();

        // Update the ckEntity
        CkEntity updatedCkEntity = ckEntityRepository.findById(ckEntity.getId()).orElseThrow();
        updatedCkEntity.name(UPDATED_NAME).value(UPDATED_VALUE).toolVersion(UPDATED_TOOL_VERSION);
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(updatedCkEntity);

        restCkEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ckEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
        CkEntity testCkEntity = ckEntityList.get(ckEntityList.size() - 1);
        assertThat(testCkEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCkEntity.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCkEntity.getToolVersion()).isEqualTo(UPDATED_TOOL_VERSION);
    }

    @Test
    void putNonExistingCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ckEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ckEntityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCkEntityWithPatch() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();

        // Update the ckEntity using partial update
        CkEntity partialUpdatedCkEntity = new CkEntity();
        partialUpdatedCkEntity.setId(ckEntity.getId());

        partialUpdatedCkEntity.name(UPDATED_NAME).toolVersion(UPDATED_TOOL_VERSION);

        restCkEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCkEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCkEntity))
            )
            .andExpect(status().isOk());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
        CkEntity testCkEntity = ckEntityList.get(ckEntityList.size() - 1);
        assertThat(testCkEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCkEntity.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testCkEntity.getToolVersion()).isEqualTo(UPDATED_TOOL_VERSION);
    }

    @Test
    void fullUpdateCkEntityWithPatch() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();

        // Update the ckEntity using partial update
        CkEntity partialUpdatedCkEntity = new CkEntity();
        partialUpdatedCkEntity.setId(ckEntity.getId());

        partialUpdatedCkEntity.name(UPDATED_NAME).value(UPDATED_VALUE).toolVersion(UPDATED_TOOL_VERSION);

        restCkEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCkEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCkEntity))
            )
            .andExpect(status().isOk());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
        CkEntity testCkEntity = ckEntityList.get(ckEntityList.size() - 1);
        assertThat(testCkEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCkEntity.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testCkEntity.getToolVersion()).isEqualTo(UPDATED_TOOL_VERSION);
    }

    @Test
    void patchNonExistingCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ckEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCkEntity() throws Exception {
        int databaseSizeBeforeUpdate = ckEntityRepository.findAll().size();
        ckEntity.setId(UUID.randomUUID().toString());

        // Create the CkEntity
        CkEntityDTO ckEntityDTO = ckEntityMapper.toDto(ckEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCkEntityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ckEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CkEntity in the database
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCkEntity() throws Exception {
        // Initialize the database
        ckEntityRepository.save(ckEntity);

        int databaseSizeBeforeDelete = ckEntityRepository.findAll().size();

        // Delete the ckEntity
        restCkEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, ckEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CkEntity> ckEntityList = ckEntityRepository.findAll();
        assertThat(ckEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
