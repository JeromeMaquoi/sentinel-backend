package com.snail.sentinel.backend.web.rest;

import static com.snail.sentinel.backend.domain.StackTraceElementJoularEntityAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.IntegrationTest;
import com.snail.sentinel.backend.domain.StackTraceElementJoularEntity;
import com.snail.sentinel.backend.repository.StackTraceElementJoularEntityRepository;
import com.snail.sentinel.backend.service.dto.AttributeEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementJoularEntityDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.ConstructorElementDTO;
import com.snail.sentinel.backend.service.mapper.StackTraceElementJoularEntityMapper;

import java.util.List;
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
 * Integration tests for the {@link StackTraceElementJoularEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StackTraceElementJoularEntityResourceIT {

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final AttributeEntityDTO DEFAULT_ATTRIBUTE_ENTITY_DTO = new AttributeEntityDTO().withName("test").withType("type").withActualType("actualType");
    private static final AttributeEntityDTO UPDATED_ATTRIBUTE_ENTITY_DTO = new AttributeEntityDTO().withName("test_update").withType("type update").withActualType("actual type update");
    private static final ConstructorElementDTO DEFAULT_CONSTRUCTOR_ELEMENT = new ConstructorElementDTO().attributes(List.of(DEFAULT_ATTRIBUTE_ENTITY_DTO));
    private static final ConstructorElementDTO UPDATED_CONSTRUCTOR_ELEMENT = new ConstructorElementDTO().attributes(List.of(UPDATED_ATTRIBUTE_ENTITY_DTO));

    private static final String DEFAULT_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_ANCESTORS = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTORS = "BBBBBBBBBB";

    private static final String DEFAULT_CONSUMPTION_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMPTION_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_COMMIT = "AAAAAAAAAA";
    private static final String UPDATED_COMMIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stack-trace-element-joular-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StackTraceElementJoularEntityRepository stackTraceElementJoularEntityRepository;

    @Autowired
    private StackTraceElementJoularEntityMapper stackTraceElementJoularEntityMapper;

    @Autowired
    private MockMvc restStackTraceElementJoularEntityMockMvc;

    private StackTraceElementJoularEntity stackTraceElementJoularEntity;

    private StackTraceElementJoularEntity insertedStackTraceElementJoularEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StackTraceElementJoularEntity createEntity() {
        return new StackTraceElementJoularEntity()
            .lineNumber(DEFAULT_LINE_NUMBER)
            .constructorElement(DEFAULT_CONSTRUCTOR_ELEMENT)
            .parent(DEFAULT_PARENT)
            .ancestors(DEFAULT_ANCESTORS)
            .consumptionValues(DEFAULT_CONSUMPTION_VALUES)
            .commit(DEFAULT_COMMIT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StackTraceElementJoularEntity createUpdatedEntity() {
        return new StackTraceElementJoularEntity()
            .lineNumber(UPDATED_LINE_NUMBER)
            .constructorElement(UPDATED_CONSTRUCTOR_ELEMENT)
            .parent(UPDATED_PARENT)
            .ancestors(UPDATED_ANCESTORS)
            .consumptionValues(UPDATED_CONSUMPTION_VALUES)
            .commit(UPDATED_COMMIT);
    }

    @BeforeEach
    public void initTest() {
        stackTraceElementJoularEntity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStackTraceElementJoularEntity != null) {
            stackTraceElementJoularEntityRepository.delete(insertedStackTraceElementJoularEntity);
            insertedStackTraceElementJoularEntity = null;
        }
    }

    @Test
    void createStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );
        var returnedStackTraceElementJoularEntityDTO = om.readValue(
            restStackTraceElementJoularEntityMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StackTraceElementJoularEntityDTO.class
        );

        // Validate the StackTraceElementJoularEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStackTraceElementJoularEntity = stackTraceElementJoularEntityMapper.toEntity(returnedStackTraceElementJoularEntityDTO);
        assertStackTraceElementJoularEntityUpdatableFieldsEquals(
            returnedStackTraceElementJoularEntity,
            getPersistedStackTraceElementJoularEntity(returnedStackTraceElementJoularEntity)
        );

        insertedStackTraceElementJoularEntity = returnedStackTraceElementJoularEntity;
    }

    @Test
    void createStackTraceElementJoularEntityWithExistingId() throws Exception {
        // Create the StackTraceElementJoularEntity with an existing ID
        stackTraceElementJoularEntity.setId("existing_id");
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStackTraceElementJoularEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStackTraceElementJoularEntities() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        // Get all the stackTraceElementJoularEntityList
        restStackTraceElementJoularEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stackTraceElementJoularEntity.getId())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].constructorElement").value(hasItem(DEFAULT_CONSTRUCTOR_ELEMENT)))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT)))
            .andExpect(jsonPath("$.[*].ancestors").value(hasItem(DEFAULT_ANCESTORS)))
            .andExpect(jsonPath("$.[*].consumptionValues").value(hasItem(DEFAULT_CONSUMPTION_VALUES)))
            .andExpect(jsonPath("$.[*].commit").value(hasItem(DEFAULT_COMMIT)));
    }

    @Test
    void getStackTraceElementJoularEntity() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        // Get the stackTraceElementJoularEntity
        restStackTraceElementJoularEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, stackTraceElementJoularEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stackTraceElementJoularEntity.getId()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.constructorElement").value(DEFAULT_CONSTRUCTOR_ELEMENT))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT))
            .andExpect(jsonPath("$.ancestors").value(DEFAULT_ANCESTORS))
            .andExpect(jsonPath("$.consumptionValues").value(DEFAULT_CONSUMPTION_VALUES))
            .andExpect(jsonPath("$.commit").value(DEFAULT_COMMIT));
    }

    @Test
    void getNonExistingStackTraceElementJoularEntity() throws Exception {
        // Get the stackTraceElementJoularEntity
        restStackTraceElementJoularEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingStackTraceElementJoularEntity() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stackTraceElementJoularEntity
        StackTraceElementJoularEntity updatedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository
            .findById(stackTraceElementJoularEntity.getId())
            .orElseThrow();
        updatedStackTraceElementJoularEntity
            .lineNumber(UPDATED_LINE_NUMBER)
            .constructorElement(UPDATED_CONSTRUCTOR_ELEMENT)
            .parent(UPDATED_PARENT)
            .ancestors(UPDATED_ANCESTORS)
            .consumptionValues(UPDATED_CONSUMPTION_VALUES)
            .commit(UPDATED_COMMIT);
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            updatedStackTraceElementJoularEntity
        );

        restStackTraceElementJoularEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stackTraceElementJoularEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStackTraceElementJoularEntityToMatchAllProperties(updatedStackTraceElementJoularEntity);
    }

    @Test
    void putNonExistingStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stackTraceElementJoularEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    /*@Test
    void partialUpdateStackTraceElementJoularEntityWithPatch() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stackTraceElementJoularEntity using partial update
        StackTraceElementJoularEntity partialUpdatedStackTraceElementJoularEntity = new StackTraceElementJoularEntity();
        partialUpdatedStackTraceElementJoularEntity.setId(stackTraceElementJoularEntity.getId());

        partialUpdatedStackTraceElementJoularEntity.lineNumber(UPDATED_LINE_NUMBER).parent(UPDATED_PARENT).ancestors(UPDATED_ANCESTORS);

        restStackTraceElementJoularEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStackTraceElementJoularEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStackTraceElementJoularEntity))
            )
            .andExpect(status().isOk());

        // Validate the StackTraceElementJoularEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStackTraceElementJoularEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStackTraceElementJoularEntity, stackTraceElementJoularEntity),
            getPersistedStackTraceElementJoularEntity(stackTraceElementJoularEntity)
        );
    }*/

    @Test
    void fullUpdateStackTraceElementJoularEntityWithPatch() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stackTraceElementJoularEntity using partial update
        StackTraceElementJoularEntity partialUpdatedStackTraceElementJoularEntity = new StackTraceElementJoularEntity();
        partialUpdatedStackTraceElementJoularEntity.setId(stackTraceElementJoularEntity.getId());

        partialUpdatedStackTraceElementJoularEntity
            .lineNumber(UPDATED_LINE_NUMBER)
            .constructorElement(UPDATED_CONSTRUCTOR_ELEMENT)
            .parent(UPDATED_PARENT)
            .ancestors(UPDATED_ANCESTORS)
            .consumptionValues(UPDATED_CONSUMPTION_VALUES)
            .commit(UPDATED_COMMIT);

        restStackTraceElementJoularEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStackTraceElementJoularEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStackTraceElementJoularEntity))
            )
            .andExpect(status().isOk());

        // Validate the StackTraceElementJoularEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStackTraceElementJoularEntityUpdatableFieldsEquals(
            partialUpdatedStackTraceElementJoularEntity,
            getPersistedStackTraceElementJoularEntity(partialUpdatedStackTraceElementJoularEntity)
        );
    }

    @Test
    void patchNonExistingStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stackTraceElementJoularEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStackTraceElementJoularEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stackTraceElementJoularEntity.setId(UUID.randomUUID().toString());

        // Create the StackTraceElementJoularEntity
        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityMapper.toDto(
            stackTraceElementJoularEntity
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStackTraceElementJoularEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stackTraceElementJoularEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StackTraceElementJoularEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStackTraceElementJoularEntity() throws Exception {
        // Initialize the database
        insertedStackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stackTraceElementJoularEntity
        restStackTraceElementJoularEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, stackTraceElementJoularEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stackTraceElementJoularEntityRepository.count();
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

    protected StackTraceElementJoularEntity getPersistedStackTraceElementJoularEntity(
        StackTraceElementJoularEntity stackTraceElementJoularEntity
    ) {
        return stackTraceElementJoularEntityRepository.findById(stackTraceElementJoularEntity.getId()).orElseThrow();
    }

    protected void assertPersistedStackTraceElementJoularEntityToMatchAllProperties(
        StackTraceElementJoularEntity expectedStackTraceElementJoularEntity
    ) {
        assertStackTraceElementJoularEntityAllPropertiesEquals(
            expectedStackTraceElementJoularEntity,
            getPersistedStackTraceElementJoularEntity(expectedStackTraceElementJoularEntity)
        );
    }

    protected void assertPersistedStackTraceElementJoularEntityToMatchUpdatableProperties(
        StackTraceElementJoularEntity expectedStackTraceElementJoularEntity
    ) {
        assertStackTraceElementJoularEntityAllUpdatablePropertiesEquals(
            expectedStackTraceElementJoularEntity,
            getPersistedStackTraceElementJoularEntity(expectedStackTraceElementJoularEntity)
        );
    }
}
