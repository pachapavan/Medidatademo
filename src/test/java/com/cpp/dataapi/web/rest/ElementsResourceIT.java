package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Elements;
import com.cpp.dataapi.domain.enumeration.ElementType;
import com.cpp.dataapi.repository.ElementsRepository;
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
 * Integration tests for the {@link ElementsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElementsResourceIT {

    private static final ElementType DEFAULT_TYPE = ElementType.Text;
    private static final ElementType UPDATED_TYPE = ElementType.Button;

    private static final String ENTITY_API_URL = "/api/elements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ElementsRepository elementsRepository;

    @Autowired
    private MockMvc restElementsMockMvc;

    private Elements elements;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elements createEntity() {
        Elements elements = new Elements().type(DEFAULT_TYPE);
        return elements;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elements createUpdatedEntity() {
        Elements elements = new Elements().type(UPDATED_TYPE);
        return elements;
    }

    @BeforeEach
    public void initTest() {
        elementsRepository.deleteAll();
        elements = createEntity();
    }

    @Test
    void createElements() throws Exception {
        int databaseSizeBeforeCreate = elementsRepository.findAll().size();
        // Create the Elements
        restElementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isCreated());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeCreate + 1);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createElementsWithExistingId() throws Exception {
        // Create the Elements with an existing ID
        elements.setId("existing_id");

        int databaseSizeBeforeCreate = elementsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllElements() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        // Get all the elementsList
        restElementsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elements.getId())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    void getElements() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        // Get the elements
        restElementsMockMvc
            .perform(get(ENTITY_API_URL_ID, elements.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(elements.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingElements() throws Exception {
        // Get the elements
        restElementsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewElements() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements
        Elements updatedElements = elementsRepository.findById(elements.getId()).get();
        updatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedElements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateElementsWithPatch() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements using partial update
        Elements partialUpdatedElements = new Elements();
        partialUpdatedElements.setId(elements.getId());

        partialUpdatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateElementsWithPatch() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();

        // Update the elements using partial update
        Elements partialUpdatedElements = new Elements();
        partialUpdatedElements.setId(elements.getId());

        partialUpdatedElements.type(UPDATED_TYPE);

        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedElements))
            )
            .andExpect(status().isOk());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
        Elements testElements = elementsList.get(elementsList.size() - 1);
        assertThat(testElements.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, elements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(elements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamElements() throws Exception {
        int databaseSizeBeforeUpdate = elementsRepository.findAll().size();
        elements.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(elements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elements in the database
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteElements() throws Exception {
        // Initialize the database
        elementsRepository.save(elements);

        int databaseSizeBeforeDelete = elementsRepository.findAll().size();

        // Delete the elements
        restElementsMockMvc
            .perform(delete(ENTITY_API_URL_ID, elements.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Elements> elementsList = elementsRepository.findAll();
        assertThat(elementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
