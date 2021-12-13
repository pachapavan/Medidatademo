package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Spacing;
import com.cpp.dataapi.repository.SpacingRepository;
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
 * Integration tests for the {@link SpacingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpacingResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spacings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SpacingRepository spacingRepository;

    @Autowired
    private MockMvc restSpacingMockMvc;

    private Spacing spacing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spacing createEntity() {
        Spacing spacing = new Spacing().className(DEFAULT_CLASS_NAME);
        return spacing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spacing createUpdatedEntity() {
        Spacing spacing = new Spacing().className(UPDATED_CLASS_NAME);
        return spacing;
    }

    @BeforeEach
    public void initTest() {
        spacingRepository.deleteAll();
        spacing = createEntity();
    }

    @Test
    void createSpacing() throws Exception {
        int databaseSizeBeforeCreate = spacingRepository.findAll().size();
        // Create the Spacing
        restSpacingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isCreated());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeCreate + 1);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
    }

    @Test
    void createSpacingWithExistingId() throws Exception {
        // Create the Spacing with an existing ID
        spacing.setId("existing_id");

        int databaseSizeBeforeCreate = spacingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpacingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSpacings() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        // Get all the spacingList
        restSpacingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spacing.getId())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)));
    }

    @Test
    void getSpacing() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        // Get the spacing
        restSpacingMockMvc
            .perform(get(ENTITY_API_URL_ID, spacing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spacing.getId()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME));
    }

    @Test
    void getNonExistingSpacing() throws Exception {
        // Get the spacing
        restSpacingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewSpacing() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing
        Spacing updatedSpacing = spacingRepository.findById(spacing.getId()).get();
        updatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpacing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    void putNonExistingSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spacing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSpacingWithPatch() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing using partial update
        Spacing partialUpdatedSpacing = new Spacing();
        partialUpdatedSpacing.setId(spacing.getId());

        partialUpdatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    void fullUpdateSpacingWithPatch() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();

        // Update the spacing using partial update
        Spacing partialUpdatedSpacing = new Spacing();
        partialUpdatedSpacing.setId(spacing.getId());

        partialUpdatedSpacing.className(UPDATED_CLASS_NAME);

        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpacing))
            )
            .andExpect(status().isOk());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
        Spacing testSpacing = spacingList.get(spacingList.size() - 1);
        assertThat(testSpacing.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    }

    @Test
    void patchNonExistingSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spacing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spacing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSpacing() throws Exception {
        int databaseSizeBeforeUpdate = spacingRepository.findAll().size();
        spacing.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpacingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spacing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spacing in the database
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSpacing() throws Exception {
        // Initialize the database
        spacingRepository.save(spacing);

        int databaseSizeBeforeDelete = spacingRepository.findAll().size();

        // Delete the spacing
        restSpacingMockMvc
            .perform(delete(ENTITY_API_URL_ID, spacing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spacing> spacingList = spacingRepository.findAll();
        assertThat(spacingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
