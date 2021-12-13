package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.DisplayAtt;
import com.cpp.dataapi.domain.enumeration.ElementType;
import com.cpp.dataapi.repository.DisplayAttRepository;
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
 * Integration tests for the {@link DisplayAttResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisplayAttResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ElementType DEFAULT_TYPE = ElementType.Text;
    private static final ElementType UPDATED_TYPE = ElementType.Button;

    private static final String ENTITY_API_URL = "/api/display-atts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DisplayAttRepository displayAttRepository;

    @Autowired
    private MockMvc restDisplayAttMockMvc;

    private DisplayAtt displayAtt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisplayAtt createEntity() {
        DisplayAtt displayAtt = new DisplayAtt().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return displayAtt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisplayAtt createUpdatedEntity() {
        DisplayAtt displayAtt = new DisplayAtt().name(UPDATED_NAME).type(UPDATED_TYPE);
        return displayAtt;
    }

    @BeforeEach
    public void initTest() {
        displayAttRepository.deleteAll();
        displayAtt = createEntity();
    }

    @Test
    void createDisplayAtt() throws Exception {
        int databaseSizeBeforeCreate = displayAttRepository.findAll().size();
        // Create the DisplayAtt
        restDisplayAttMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isCreated());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeCreate + 1);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createDisplayAttWithExistingId() throws Exception {
        // Create the DisplayAtt with an existing ID
        displayAtt.setId("existing_id");

        int databaseSizeBeforeCreate = displayAttRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisplayAttMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDisplayAtts() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        // Get all the displayAttList
        restDisplayAttMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(displayAtt.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    void getDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        // Get the displayAtt
        restDisplayAttMockMvc
            .perform(get(ENTITY_API_URL_ID, displayAtt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(displayAtt.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingDisplayAtt() throws Exception {
        // Get the displayAtt
        restDisplayAttMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt
        DisplayAtt updatedDisplayAtt = displayAttRepository.findById(displayAtt.getId()).get();
        updatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisplayAtt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, displayAtt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(displayAtt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDisplayAttWithPatch() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt using partial update
        DisplayAtt partialUpdatedDisplayAtt = new DisplayAtt();
        partialUpdatedDisplayAtt.setId(displayAtt.getId());

        partialUpdatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisplayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateDisplayAttWithPatch() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();

        // Update the displayAtt using partial update
        DisplayAtt partialUpdatedDisplayAtt = new DisplayAtt();
        partialUpdatedDisplayAtt.setId(displayAtt.getId());

        partialUpdatedDisplayAtt.name(UPDATED_NAME).type(UPDATED_TYPE);

        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisplayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisplayAtt))
            )
            .andExpect(status().isOk());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
        DisplayAtt testDisplayAtt = displayAttList.get(displayAttList.size() - 1);
        assertThat(testDisplayAtt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDisplayAtt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, displayAtt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDisplayAtt() throws Exception {
        int databaseSizeBeforeUpdate = displayAttRepository.findAll().size();
        displayAtt.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisplayAttMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(displayAtt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisplayAtt in the database
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDisplayAtt() throws Exception {
        // Initialize the database
        displayAttRepository.save(displayAtt);

        int databaseSizeBeforeDelete = displayAttRepository.findAll().size();

        // Delete the displayAtt
        restDisplayAttMockMvc
            .perform(delete(ENTITY_API_URL_ID, displayAtt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisplayAtt> displayAttList = displayAttRepository.findAll();
        assertThat(displayAttList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
