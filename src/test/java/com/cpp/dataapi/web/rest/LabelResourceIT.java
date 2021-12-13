package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Label;
import com.cpp.dataapi.repository.LabelRepository;
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
 * Integration tests for the {@link LabelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LabelResourceIT {

    private static final String ENTITY_API_URL = "/api/labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MockMvc restLabelMockMvc;

    private Label label;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createEntity() {
        Label label = new Label();
        return label;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Label createUpdatedEntity() {
        Label label = new Label();
        return label;
    }

    @BeforeEach
    public void initTest() {
        labelRepository.deleteAll();
        label = createEntity();
    }

    @Test
    void createLabel() throws Exception {
        int databaseSizeBeforeCreate = labelRepository.findAll().size();
        // Create the Label
        restLabelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label)))
            .andExpect(status().isCreated());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate + 1);
        Label testLabel = labelList.get(labelList.size() - 1);
    }

    @Test
    void createLabelWithExistingId() throws Exception {
        // Create the Label with an existing ID
        label.setId("existing_id");

        int databaseSizeBeforeCreate = labelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label)))
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLabels() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        // Get all the labelList
        restLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(label.getId())));
    }

    @Test
    void getLabel() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        // Get the label
        restLabelMockMvc
            .perform(get(ENTITY_API_URL_ID, label.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(label.getId()));
    }

    @Test
    void getNonExistingLabel() throws Exception {
        // Get the label
        restLabelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewLabel() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label
        Label updatedLabel = labelRepository.findById(label.getId()).get();

        restLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLabel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
    }

    @Test
    void putNonExistingLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, label.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(label)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        restLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
    }

    @Test
    void fullUpdateLabelWithPatch() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        int databaseSizeBeforeUpdate = labelRepository.findAll().size();

        // Update the label using partial update
        Label partialUpdatedLabel = new Label();
        partialUpdatedLabel.setId(label.getId());

        restLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLabel))
            )
            .andExpect(status().isOk());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
        Label testLabel = labelList.get(labelList.size() - 1);
    }

    @Test
    void patchNonExistingLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, label.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(label))
            )
            .andExpect(status().isBadRequest());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLabel() throws Exception {
        int databaseSizeBeforeUpdate = labelRepository.findAll().size();
        label.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(label)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Label in the database
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLabel() throws Exception {
        // Initialize the database
        labelRepository.save(label);

        int databaseSizeBeforeDelete = labelRepository.findAll().size();

        // Delete the label
        restLabelMockMvc
            .perform(delete(ENTITY_API_URL_ID, label.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Label> labelList = labelRepository.findAll();
        assertThat(labelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
