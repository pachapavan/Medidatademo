package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Head;
import com.cpp.dataapi.repository.HeadRepository;
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
 * Integration tests for the {@link HeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HeadResourceIT {

    private static final String ENTITY_API_URL = "/api/heads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private HeadRepository headRepository;

    @Autowired
    private MockMvc restHeadMockMvc;

    private Head head;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Head createEntity() {
        Head head = new Head();
        return head;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Head createUpdatedEntity() {
        Head head = new Head();
        return head;
    }

    @BeforeEach
    public void initTest() {
        headRepository.deleteAll();
        head = createEntity();
    }

    @Test
    void createHead() throws Exception {
        int databaseSizeBeforeCreate = headRepository.findAll().size();
        // Create the Head
        restHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isCreated());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeCreate + 1);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    void createHeadWithExistingId() throws Exception {
        // Create the Head with an existing ID
        head.setId("existing_id");

        int databaseSizeBeforeCreate = headRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHeads() throws Exception {
        // Initialize the database
        headRepository.save(head);

        // Get all the headList
        restHeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(head.getId())));
    }

    @Test
    void getHead() throws Exception {
        // Initialize the database
        headRepository.save(head);

        // Get the head
        restHeadMockMvc
            .perform(get(ENTITY_API_URL_ID, head.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(head.getId()));
    }

    @Test
    void getNonExistingHead() throws Exception {
        // Get the head
        restHeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewHead() throws Exception {
        // Initialize the database
        headRepository.save(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head
        Head updatedHead = headRepository.findById(head.getId()).get();

        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHead.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    void putNonExistingHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, head.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHeadWithPatch() throws Exception {
        // Initialize the database
        headRepository.save(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head using partial update
        Head partialUpdatedHead = new Head();
        partialUpdatedHead.setId(head.getId());

        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    void fullUpdateHeadWithPatch() throws Exception {
        // Initialize the database
        headRepository.save(head);

        int databaseSizeBeforeUpdate = headRepository.findAll().size();

        // Update the head using partial update
        Head partialUpdatedHead = new Head();
        partialUpdatedHead.setId(head.getId());

        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHead))
            )
            .andExpect(status().isOk());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
        Head testHead = headList.get(headList.size() - 1);
    }

    @Test
    void patchNonExistingHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, head.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(head))
            )
            .andExpect(status().isBadRequest());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHead() throws Exception {
        int databaseSizeBeforeUpdate = headRepository.findAll().size();
        head.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHeadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(head)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Head in the database
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHead() throws Exception {
        // Initialize the database
        headRepository.save(head);

        int databaseSizeBeforeDelete = headRepository.findAll().size();

        // Delete the head
        restHeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, head.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Head> headList = headRepository.findAll();
        assertThat(headList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
