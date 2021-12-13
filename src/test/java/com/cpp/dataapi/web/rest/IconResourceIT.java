package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Icon;
import com.cpp.dataapi.repository.IconRepository;
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
 * Integration tests for the {@link IconResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IconResourceIT {

    private static final String DEFAULT_ARIA_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_ARIA_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/icons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private IconRepository iconRepository;

    @Autowired
    private MockMvc restIconMockMvc;

    private Icon icon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icon createEntity() {
        Icon icon = new Icon().ariaLabel(DEFAULT_ARIA_LABEL);
        return icon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icon createUpdatedEntity() {
        Icon icon = new Icon().ariaLabel(UPDATED_ARIA_LABEL);
        return icon;
    }

    @BeforeEach
    public void initTest() {
        iconRepository.deleteAll();
        icon = createEntity();
    }

    @Test
    void createIcon() throws Exception {
        int databaseSizeBeforeCreate = iconRepository.findAll().size();
        // Create the Icon
        restIconMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icon)))
            .andExpect(status().isCreated());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeCreate + 1);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getAriaLabel()).isEqualTo(DEFAULT_ARIA_LABEL);
    }

    @Test
    void createIconWithExistingId() throws Exception {
        // Create the Icon with an existing ID
        icon.setId("existing_id");

        int databaseSizeBeforeCreate = iconRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIconMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icon)))
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIcons() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        // Get all the iconList
        restIconMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icon.getId())))
            .andExpect(jsonPath("$.[*].ariaLabel").value(hasItem(DEFAULT_ARIA_LABEL)));
    }

    @Test
    void getIcon() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        // Get the icon
        restIconMockMvc
            .perform(get(ENTITY_API_URL_ID, icon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(icon.getId()))
            .andExpect(jsonPath("$.ariaLabel").value(DEFAULT_ARIA_LABEL));
    }

    @Test
    void getNonExistingIcon() throws Exception {
        // Get the icon
        restIconMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewIcon() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        int databaseSizeBeforeUpdate = iconRepository.findAll().size();

        // Update the icon
        Icon updatedIcon = iconRepository.findById(icon.getId()).get();
        updatedIcon.ariaLabel(UPDATED_ARIA_LABEL);

        restIconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIcon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIcon))
            )
            .andExpect(status().isOk());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getAriaLabel()).isEqualTo(UPDATED_ARIA_LABEL);
    }

    @Test
    void putNonExistingIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIconWithPatch() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        int databaseSizeBeforeUpdate = iconRepository.findAll().size();

        // Update the icon using partial update
        Icon partialUpdatedIcon = new Icon();
        partialUpdatedIcon.setId(icon.getId());

        restIconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcon))
            )
            .andExpect(status().isOk());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getAriaLabel()).isEqualTo(DEFAULT_ARIA_LABEL);
    }

    @Test
    void fullUpdateIconWithPatch() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        int databaseSizeBeforeUpdate = iconRepository.findAll().size();

        // Update the icon using partial update
        Icon partialUpdatedIcon = new Icon();
        partialUpdatedIcon.setId(icon.getId());

        partialUpdatedIcon.ariaLabel(UPDATED_ARIA_LABEL);

        restIconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcon))
            )
            .andExpect(status().isOk());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getAriaLabel()).isEqualTo(UPDATED_ARIA_LABEL);
    }

    @Test
    void patchNonExistingIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, icon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();
        icon.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIconMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(icon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIcon() throws Exception {
        // Initialize the database
        iconRepository.save(icon);

        int databaseSizeBeforeDelete = iconRepository.findAll().size();

        // Delete the icon
        restIconMockMvc
            .perform(delete(ENTITY_API_URL_ID, icon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
