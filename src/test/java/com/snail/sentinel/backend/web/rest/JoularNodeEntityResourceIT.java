package com.snail.sentinel.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;
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
 * Integration tests for the {@link JoularNodeEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JoularNodeEntityResourceIT {

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final String ENTITY_API_URL = "/api/joular-node-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private JoularNodeEntityRepository joularNodeEntityRepository;

    @Autowired
    private JoularNodeEntityMapper joularNodeEntityMapper;

    @Autowired
    private MockMvc restJoularNodeEntityMockMvc;

    private JoularNodeEntity joularNodeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JoularNodeEntity createEntity() {
        JoularNodeEntity joularNodeEntity = new JoularNodeEntity().lineNumber(DEFAULT_LINE_NUMBER).value(DEFAULT_VALUE);
        return joularNodeEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JoularNodeEntity createUpdatedEntity() {
        JoularNodeEntity joularNodeEntity = new JoularNodeEntity().lineNumber(UPDATED_LINE_NUMBER).value(UPDATED_VALUE);
        return joularNodeEntity;
    }

    @BeforeEach
    public void initTest() {
        joularNodeEntityRepository.deleteAll();
        joularNodeEntity = createEntity();
    }

    @Test
    void createJoularNodeEntity() throws Exception {
        int databaseSizeBeforeCreate = joularNodeEntityRepository.findAll().size();
        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);
        restJoularNodeEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeCreate + 1);
        JoularNodeEntity testJoularNodeEntity = joularNodeEntityList.get(joularNodeEntityList.size() - 1);
        assertThat(testJoularNodeEntity.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testJoularNodeEntity.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createJoularNodeEntityWithExistingId() throws Exception {
        // Create the JoularNodeEntity with an existing ID
        joularNodeEntity.setId("existing_id");
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        int databaseSizeBeforeCreate = joularNodeEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoularNodeEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllJoularNodeEntities() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        // Get all the joularNodeEntityList
        restJoularNodeEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joularNodeEntity.getId())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    void getJoularNodeEntity() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        // Get the joularNodeEntity
        restJoularNodeEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, joularNodeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(joularNodeEntity.getId()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    void getNonExistingJoularNodeEntity() throws Exception {
        // Get the joularNodeEntity
        restJoularNodeEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingJoularNodeEntity() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();

        // Update the joularNodeEntity
        JoularNodeEntity updatedJoularNodeEntity = joularNodeEntityRepository.findById(joularNodeEntity.getId()).orElseThrow();
        updatedJoularNodeEntity.lineNumber(UPDATED_LINE_NUMBER).value(UPDATED_VALUE);
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(updatedJoularNodeEntity);

        restJoularNodeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, joularNodeEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
        JoularNodeEntity testJoularNodeEntity = joularNodeEntityList.get(joularNodeEntityList.size() - 1);
        assertThat(testJoularNodeEntity.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testJoularNodeEntity.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, joularNodeEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJoularNodeEntityWithPatch() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();

        // Update the joularNodeEntity using partial update
        JoularNodeEntity partialUpdatedJoularNodeEntity = new JoularNodeEntity();
        partialUpdatedJoularNodeEntity.setId(joularNodeEntity.getId());

        partialUpdatedJoularNodeEntity.lineNumber(UPDATED_LINE_NUMBER);

        restJoularNodeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJoularNodeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoularNodeEntity))
            )
            .andExpect(status().isOk());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
        JoularNodeEntity testJoularNodeEntity = joularNodeEntityList.get(joularNodeEntityList.size() - 1);
        assertThat(testJoularNodeEntity.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testJoularNodeEntity.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void fullUpdateJoularNodeEntityWithPatch() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();

        // Update the joularNodeEntity using partial update
        JoularNodeEntity partialUpdatedJoularNodeEntity = new JoularNodeEntity();
        partialUpdatedJoularNodeEntity.setId(joularNodeEntity.getId());

        partialUpdatedJoularNodeEntity.lineNumber(UPDATED_LINE_NUMBER).value(UPDATED_VALUE);

        restJoularNodeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJoularNodeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoularNodeEntity))
            )
            .andExpect(status().isOk());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
        JoularNodeEntity testJoularNodeEntity = joularNodeEntityList.get(joularNodeEntityList.size() - 1);
        assertThat(testJoularNodeEntity.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testJoularNodeEntity.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, joularNodeEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJoularNodeEntity() throws Exception {
        int databaseSizeBeforeUpdate = joularNodeEntityRepository.findAll().size();
        joularNodeEntity.setId(UUID.randomUUID().toString());

        // Create the JoularNodeEntity
        JoularNodeEntityDTO joularNodeEntityDTO = joularNodeEntityMapper.toDto(joularNodeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoularNodeEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(joularNodeEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JoularNodeEntity in the database
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJoularNodeEntity() throws Exception {
        // Initialize the database
        joularNodeEntityRepository.save(joularNodeEntity);

        int databaseSizeBeforeDelete = joularNodeEntityRepository.findAll().size();

        // Delete the joularNodeEntity
        restJoularNodeEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, joularNodeEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JoularNodeEntity> joularNodeEntityList = joularNodeEntityRepository.findAll();
        assertThat(joularNodeEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
