package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.BadgeType;
import com.cpp.dataapi.domain.enumeration.ColorEnum;
import com.cpp.dataapi.repository.BadgeTypeRepository;
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
 * Integration tests for the {@link BadgeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BadgeTypeResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ColorEnum DEFAULT_TYPE = ColorEnum.Plain;
    private static final ColorEnum UPDATED_TYPE = ColorEnum.Grey;

    private static final String ENTITY_API_URL = "/api/badge-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BadgeTypeRepository badgeTypeRepository;

    @Autowired
    private MockMvc restBadgeTypeMockMvc;

    private BadgeType badgeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BadgeType createEntity() {
        BadgeType badgeType = new BadgeType().status(DEFAULT_STATUS).type(DEFAULT_TYPE);
        return badgeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BadgeType createUpdatedEntity() {
        BadgeType badgeType = new BadgeType().status(UPDATED_STATUS).type(UPDATED_TYPE);
        return badgeType;
    }

    @BeforeEach
    public void initTest() {
        badgeTypeRepository.deleteAll();
        badgeType = createEntity();
    }

    @Test
    void createBadgeType() throws Exception {
        int databaseSizeBeforeCreate = badgeTypeRepository.findAll().size();
        // Create the BadgeType
        restBadgeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isCreated());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createBadgeTypeWithExistingId() throws Exception {
        // Create the BadgeType with an existing ID
        badgeType.setId("existing_id");

        int databaseSizeBeforeCreate = badgeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBadgeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBadgeTypes() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        // Get all the badgeTypeList
        restBadgeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badgeType.getId())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    void getBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        // Get the badgeType
        restBadgeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, badgeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(badgeType.getId()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingBadgeType() throws Exception {
        // Get the badgeType
        restBadgeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType
        BadgeType updatedBadgeType = badgeTypeRepository.findById(badgeType.getId()).get();
        updatedBadgeType.status(UPDATED_STATUS).type(UPDATED_TYPE);

        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBadgeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, badgeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badgeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBadgeTypeWithPatch() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType using partial update
        BadgeType partialUpdatedBadgeType = new BadgeType();
        partialUpdatedBadgeType.setId(badgeType.getId());

        partialUpdatedBadgeType.status(UPDATED_STATUS);

        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateBadgeTypeWithPatch() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();

        // Update the badgeType using partial update
        BadgeType partialUpdatedBadgeType = new BadgeType();
        partialUpdatedBadgeType.setId(badgeType.getId());

        partialUpdatedBadgeType.status(UPDATED_STATUS).type(UPDATED_TYPE);

        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadgeType))
            )
            .andExpect(status().isOk());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
        BadgeType testBadgeType = badgeTypeList.get(badgeTypeList.size() - 1);
        assertThat(testBadgeType.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBadgeType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, badgeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBadgeType() throws Exception {
        int databaseSizeBeforeUpdate = badgeTypeRepository.findAll().size();
        badgeType.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(badgeType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BadgeType in the database
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBadgeType() throws Exception {
        // Initialize the database
        badgeTypeRepository.save(badgeType);

        int databaseSizeBeforeDelete = badgeTypeRepository.findAll().size();

        // Delete the badgeType
        restBadgeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, badgeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BadgeType> badgeTypeList = badgeTypeRepository.findAll();
        assertThat(badgeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
