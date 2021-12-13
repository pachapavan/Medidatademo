package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Badge;
import com.cpp.dataapi.domain.enumeration.ColorEnum;
import com.cpp.dataapi.repository.BadgeRepository;
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
 * Integration tests for the {@link BadgeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BadgeResourceIT {

    private static final ColorEnum DEFAULT_COLOR = ColorEnum.Plain;
    private static final ColorEnum UPDATED_COLOR = ColorEnum.Grey;

    private static final String ENTITY_API_URL = "/api/badges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private MockMvc restBadgeMockMvc;

    private Badge badge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Badge createEntity() {
        Badge badge = new Badge().color(DEFAULT_COLOR);
        return badge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Badge createUpdatedEntity() {
        Badge badge = new Badge().color(UPDATED_COLOR);
        return badge;
    }

    @BeforeEach
    public void initTest() {
        badgeRepository.deleteAll();
        badge = createEntity();
    }

    @Test
    void createBadge() throws Exception {
        int databaseSizeBeforeCreate = badgeRepository.findAll().size();
        // Create the Badge
        restBadgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badge)))
            .andExpect(status().isCreated());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeCreate + 1);
        Badge testBadge = badgeList.get(badgeList.size() - 1);
        assertThat(testBadge.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    void createBadgeWithExistingId() throws Exception {
        // Create the Badge with an existing ID
        badge.setId("existing_id");

        int databaseSizeBeforeCreate = badgeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBadgeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badge)))
            .andExpect(status().isBadRequest());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBadges() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        // Get all the badgeList
        restBadgeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(badge.getId())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    void getBadge() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        // Get the badge
        restBadgeMockMvc
            .perform(get(ENTITY_API_URL_ID, badge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(badge.getId()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    void getNonExistingBadge() throws Exception {
        // Get the badge
        restBadgeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBadge() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();

        // Update the badge
        Badge updatedBadge = badgeRepository.findById(badge.getId()).get();
        updatedBadge.color(UPDATED_COLOR);

        restBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBadge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBadge))
            )
            .andExpect(status().isOk());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
        Badge testBadge = badgeList.get(badgeList.size() - 1);
        assertThat(testBadge.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    void putNonExistingBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, badge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(badge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(badge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBadgeWithPatch() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();

        // Update the badge using partial update
        Badge partialUpdatedBadge = new Badge();
        partialUpdatedBadge.setId(badge.getId());

        restBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadge))
            )
            .andExpect(status().isOk());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
        Badge testBadge = badgeList.get(badgeList.size() - 1);
        assertThat(testBadge.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    void fullUpdateBadgeWithPatch() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();

        // Update the badge using partial update
        Badge partialUpdatedBadge = new Badge();
        partialUpdatedBadge.setId(badge.getId());

        partialUpdatedBadge.color(UPDATED_COLOR);

        restBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBadge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBadge))
            )
            .andExpect(status().isOk());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
        Badge testBadge = badgeList.get(badgeList.size() - 1);
        assertThat(testBadge.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    void patchNonExistingBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, badge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(badge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBadge() throws Exception {
        int databaseSizeBeforeUpdate = badgeRepository.findAll().size();
        badge.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBadgeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(badge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Badge in the database
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBadge() throws Exception {
        // Initialize the database
        badgeRepository.save(badge);

        int databaseSizeBeforeDelete = badgeRepository.findAll().size();

        // Delete the badge
        restBadgeMockMvc
            .perform(delete(ENTITY_API_URL_ID, badge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Badge> badgeList = badgeRepository.findAll();
        assertThat(badgeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
