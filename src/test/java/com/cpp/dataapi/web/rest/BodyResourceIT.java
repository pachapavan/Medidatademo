package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Body;
import com.cpp.dataapi.repository.BodyRepository;
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
 * Integration tests for the {@link BodyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BodyResourceIT {

    private static final String ENTITY_API_URL = "/api/bodies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private MockMvc restBodyMockMvc;

    private Body body;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Body createEntity() {
        Body body = new Body();
        return body;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Body createUpdatedEntity() {
        Body body = new Body();
        return body;
    }

    @BeforeEach
    public void initTest() {
        bodyRepository.deleteAll();
        body = createEntity();
    }

    @Test
    void createBody() throws Exception {
        int databaseSizeBeforeCreate = bodyRepository.findAll().size();
        // Create the Body
        restBodyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isCreated());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeCreate + 1);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    void createBodyWithExistingId() throws Exception {
        // Create the Body with an existing ID
        body.setId("existing_id");

        int databaseSizeBeforeCreate = bodyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBodyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBodies() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        // Get all the bodyList
        restBodyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(body.getId())));
    }

    @Test
    void getBody() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        // Get the body
        restBodyMockMvc
            .perform(get(ENTITY_API_URL_ID, body.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(body.getId()));
    }

    @Test
    void getNonExistingBody() throws Exception {
        // Get the body
        restBodyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBody() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body
        Body updatedBody = bodyRepository.findById(body.getId()).get();

        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBody.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    void putNonExistingBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, body.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBodyWithPatch() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body using partial update
        Body partialUpdatedBody = new Body();
        partialUpdatedBody.setId(body.getId());

        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBody.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    void fullUpdateBodyWithPatch() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();

        // Update the body using partial update
        Body partialUpdatedBody = new Body();
        partialUpdatedBody.setId(body.getId());

        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBody.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBody))
            )
            .andExpect(status().isOk());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
        Body testBody = bodyList.get(bodyList.size() - 1);
    }

    @Test
    void patchNonExistingBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, body.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(body))
            )
            .andExpect(status().isBadRequest());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBody() throws Exception {
        int databaseSizeBeforeUpdate = bodyRepository.findAll().size();
        body.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBodyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Body in the database
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBody() throws Exception {
        // Initialize the database
        bodyRepository.save(body);

        int databaseSizeBeforeDelete = bodyRepository.findAll().size();

        // Delete the body
        restBodyMockMvc
            .perform(delete(ENTITY_API_URL_ID, body.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Body> bodyList = bodyRepository.findAll();
        assertThat(bodyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
