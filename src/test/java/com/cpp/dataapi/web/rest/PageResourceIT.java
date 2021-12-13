package com.cpp.dataapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cpp.dataapi.IntegrationTest;
import com.cpp.dataapi.domain.Page;
import com.cpp.dataapi.repository.PageRepository;
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
 * Integration tests for the {@link PageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_ID = 1;
    private static final Integer UPDATED_PAGE_ID = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FULL_SCREEN = false;
    private static final Boolean UPDATED_FULL_SCREEN = true;

    private static final String DEFAULT_HISTORY = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private MockMvc restPageMockMvc;

    private Page page;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Page createEntity() {
        Page page = new Page()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .modelId(DEFAULT_MODEL_ID)
            .pageId(DEFAULT_PAGE_ID)
            .type(DEFAULT_TYPE)
            .fullScreen(DEFAULT_FULL_SCREEN)
            .history(DEFAULT_HISTORY);
        return page;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Page createUpdatedEntity() {
        Page page = new Page()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .modelId(UPDATED_MODEL_ID)
            .pageId(UPDATED_PAGE_ID)
            .type(UPDATED_TYPE)
            .fullScreen(UPDATED_FULL_SCREEN)
            .history(UPDATED_HISTORY);
        return page;
    }

    @BeforeEach
    public void initTest() {
        pageRepository.deleteAll();
        page = createEntity();
    }

    @Test
    void createPage() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();
        // Create the Page
        restPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isCreated());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeCreate + 1);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPage.getModelId()).isEqualTo(DEFAULT_MODEL_ID);
        assertThat(testPage.getPageId()).isEqualTo(DEFAULT_PAGE_ID);
        assertThat(testPage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPage.getFullScreen()).isEqualTo(DEFAULT_FULL_SCREEN);
        assertThat(testPage.getHistory()).isEqualTo(DEFAULT_HISTORY);
    }

    @Test
    void createPageWithExistingId() throws Exception {
        // Create the Page with an existing ID
        page.setId("existing_id");

        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isBadRequest());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPages() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        // Get all the pageList
        restPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(page.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].modelId").value(hasItem(DEFAULT_MODEL_ID)))
            .andExpect(jsonPath("$.[*].pageId").value(hasItem(DEFAULT_PAGE_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].fullScreen").value(hasItem(DEFAULT_FULL_SCREEN.booleanValue())))
            .andExpect(jsonPath("$.[*].history").value(hasItem(DEFAULT_HISTORY)));
    }

    @Test
    void getPage() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        // Get the page
        restPageMockMvc
            .perform(get(ENTITY_API_URL_ID, page.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(page.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.modelId").value(DEFAULT_MODEL_ID))
            .andExpect(jsonPath("$.pageId").value(DEFAULT_PAGE_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.fullScreen").value(DEFAULT_FULL_SCREEN.booleanValue()))
            .andExpect(jsonPath("$.history").value(DEFAULT_HISTORY));
    }

    @Test
    void getNonExistingPage() throws Exception {
        // Get the page
        restPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPage() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page
        Page updatedPage = pageRepository.findById(page.getId()).get();
        updatedPage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .modelId(UPDATED_MODEL_ID)
            .pageId(UPDATED_PAGE_ID)
            .type(UPDATED_TYPE)
            .fullScreen(UPDATED_FULL_SCREEN)
            .history(UPDATED_HISTORY);

        restPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPage))
            )
            .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPage.getModelId()).isEqualTo(UPDATED_MODEL_ID);
        assertThat(testPage.getPageId()).isEqualTo(UPDATED_PAGE_ID);
        assertThat(testPage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPage.getFullScreen()).isEqualTo(UPDATED_FULL_SCREEN);
        assertThat(testPage.getHistory()).isEqualTo(UPDATED_HISTORY);
    }

    @Test
    void putNonExistingPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, page.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(page))
            )
            .andExpect(status().isBadRequest());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(page))
            )
            .andExpect(status().isBadRequest());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePageWithPatch() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page using partial update
        Page partialUpdatedPage = new Page();
        partialUpdatedPage.setId(page.getId());

        partialUpdatedPage
            .description(UPDATED_DESCRIPTION)
            .modelId(UPDATED_MODEL_ID)
            .type(UPDATED_TYPE)
            .fullScreen(UPDATED_FULL_SCREEN)
            .history(UPDATED_HISTORY);

        restPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPage))
            )
            .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPage.getModelId()).isEqualTo(UPDATED_MODEL_ID);
        assertThat(testPage.getPageId()).isEqualTo(DEFAULT_PAGE_ID);
        assertThat(testPage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPage.getFullScreen()).isEqualTo(UPDATED_FULL_SCREEN);
        assertThat(testPage.getHistory()).isEqualTo(UPDATED_HISTORY);
    }

    @Test
    void fullUpdatePageWithPatch() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page using partial update
        Page partialUpdatedPage = new Page();
        partialUpdatedPage.setId(page.getId());

        partialUpdatedPage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .modelId(UPDATED_MODEL_ID)
            .pageId(UPDATED_PAGE_ID)
            .type(UPDATED_TYPE)
            .fullScreen(UPDATED_FULL_SCREEN)
            .history(UPDATED_HISTORY);

        restPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPage))
            )
            .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPage.getModelId()).isEqualTo(UPDATED_MODEL_ID);
        assertThat(testPage.getPageId()).isEqualTo(UPDATED_PAGE_ID);
        assertThat(testPage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPage.getFullScreen()).isEqualTo(UPDATED_FULL_SCREEN);
        assertThat(testPage.getHistory()).isEqualTo(UPDATED_HISTORY);
    }

    @Test
    void patchNonExistingPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, page.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(page))
            )
            .andExpect(status().isBadRequest());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(page))
            )
            .andExpect(status().isBadRequest());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();
        page.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePage() throws Exception {
        // Initialize the database
        pageRepository.save(page);

        int databaseSizeBeforeDelete = pageRepository.findAll().size();

        // Delete the page
        restPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, page.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
