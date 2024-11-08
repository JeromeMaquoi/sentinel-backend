package com.snail.sentinel.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
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
 * Integration tests for the {@link CommitEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommitEntityResourceIT {

    private static final String DEFAULT_SHA = "AAAAAAAAAA";
    private static final String UPDATED_SHA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commit-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CommitEntityRepository commitEntityRepository;

    @Autowired
    private MockMvc restCommitEntityMockMvc;

    private CommitEntity commitEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitEntity createEntity() {
        CommitEntity commitEntity = new CommitEntity().sha(DEFAULT_SHA);
        return commitEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitEntity createUpdatedEntity() {
        CommitEntity commitEntity = new CommitEntity().sha(UPDATED_SHA);
        return commitEntity;
    }

    @BeforeEach
    public void initTest() {
        commitEntityRepository.deleteAll();
        commitEntity = createEntity();
    }

    /*@Test
    void createCommitEntity() throws Exception {
        int databaseSizeBeforeCreate = commitEntityRepository.findAll().size();
        // Create the CommitEntity
        restCommitEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commitEntity)))
            .andExpect(status().isCreated());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeCreate + 1);
        CommitEntity testCommitEntity = commitEntityList.get(commitEntityList.size() - 1);
        assertThat(testCommitEntity.getSha()).isEqualTo(DEFAULT_SHA);
    }*/

    @Test
    void createCommitEntityWithExistingId() throws Exception {
        // Create the CommitEntity with an existing ID
        commitEntity.setId("existing_id");

        int databaseSizeBeforeCreate = commitEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commitEntity)))
            .andExpect(status().isBadRequest());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkShaIsRequired() throws Exception {
        int databaseSizeBeforeTest = commitEntityRepository.findAll().size();
        // set the field null
        commitEntity.setSha(null);

        // Create the CommitEntity, which fails.

        restCommitEntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commitEntity)))
            .andExpect(status().isBadRequest());

        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeTest);
    }

    /*@Test
    void getAllCommitEntities() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        // Get all the commitEntityList
        restCommitEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commitEntity.getId())))
            .andExpect(jsonPath("$.[*].sha").value(hasItem(DEFAULT_SHA)));
    }*/

    /*@Test
    void getCommitEntity() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        // Get the commitEntity
        restCommitEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, commitEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commitEntity.getId()))
            .andExpect(jsonPath("$.sha").value(DEFAULT_SHA));
    }*/

    @Test
    void getNonExistingCommitEntity() throws Exception {
        // Get the commitEntity
        restCommitEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    /*@Test
    void putExistingCommitEntity() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();

        // Update the commitEntity
        CommitEntity updatedCommitEntity = commitEntityRepository.findById(commitEntity.getId()).orElseThrow();
        updatedCommitEntity.sha(UPDATED_SHA);

        restCommitEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommitEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommitEntity))
            )
            .andExpect(status().isOk());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
        CommitEntity testCommitEntity = commitEntityList.get(commitEntityList.size() - 1);
        assertThat(testCommitEntity.getSha()).isEqualTo(UPDATED_SHA);
    }*/

    @Test
    void putNonExistingCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commitEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commitEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commitEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commitEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    /*@Test
    void partialUpdateCommitEntityWithPatch() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();

        // Update the commitEntity using partial update
        CommitEntity partialUpdatedCommitEntity = new CommitEntity();
        partialUpdatedCommitEntity.setId(commitEntity.getId());

        partialUpdatedCommitEntity.sha(UPDATED_SHA);

        restCommitEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommitEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommitEntity))
            )
            .andExpect(status().isOk());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
        CommitEntity testCommitEntity = commitEntityList.get(commitEntityList.size() - 1);
        assertThat(testCommitEntity.getSha()).isEqualTo(UPDATED_SHA);
    }*/

    /*@Test
    void fullUpdateCommitEntityWithPatch() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();

        // Update the commitEntity using partial update
        CommitEntity partialUpdatedCommitEntity = new CommitEntity();
        partialUpdatedCommitEntity.setId(commitEntity.getId());

        partialUpdatedCommitEntity.sha(UPDATED_SHA);

        restCommitEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommitEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommitEntity))
            )
            .andExpect(status().isOk());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
        CommitEntity testCommitEntity = commitEntityList.get(commitEntityList.size() - 1);
        assertThat(testCommitEntity.getSha()).isEqualTo(UPDATED_SHA);
    }*/

    @Test
    void patchNonExistingCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commitEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commitEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commitEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommitEntity() throws Exception {
        int databaseSizeBeforeUpdate = commitEntityRepository.findAll().size();
        commitEntity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommitEntityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commitEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommitEntity in the database
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    /*@Test
    void deleteCommitEntity() throws Exception {
        // Initialize the database
        commitEntityRepository.save(commitEntity);

        int databaseSizeBeforeDelete = commitEntityRepository.findAll().size();

        // Delete the commitEntity
        restCommitEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, commitEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitEntity> commitEntityList = commitEntityRepository.findAll();
        assertThat(commitEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
