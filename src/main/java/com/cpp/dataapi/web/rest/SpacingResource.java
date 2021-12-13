package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Spacing;
import com.cpp.dataapi.repository.SpacingRepository;
import com.cpp.dataapi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cpp.dataapi.domain.Spacing}.
 */
@RestController
@RequestMapping("/api")
public class SpacingResource {

    private final Logger log = LoggerFactory.getLogger(SpacingResource.class);

    private static final String ENTITY_NAME = "spacing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpacingRepository spacingRepository;

    public SpacingResource(SpacingRepository spacingRepository) {
        this.spacingRepository = spacingRepository;
    }

    /**
     * {@code POST  /spacings} : Create a new spacing.
     *
     * @param spacing the spacing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spacing, or with status {@code 400 (Bad Request)} if the spacing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spacings")
    public ResponseEntity<Spacing> createSpacing(@RequestBody Spacing spacing) throws URISyntaxException {
        log.debug("REST request to save Spacing : {}", spacing);
        if (spacing.getId() != null) {
            throw new BadRequestAlertException("A new spacing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spacing result = spacingRepository.save(spacing);
        return ResponseEntity
            .created(new URI("/api/spacings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /spacings/:id} : Updates an existing spacing.
     *
     * @param id the id of the spacing to save.
     * @param spacing the spacing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spacing,
     * or with status {@code 400 (Bad Request)} if the spacing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spacing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spacings/{id}")
    public ResponseEntity<Spacing> updateSpacing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Spacing spacing
    ) throws URISyntaxException {
        log.debug("REST request to update Spacing : {}, {}", id, spacing);
        if (spacing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spacing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spacingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Spacing result = spacingRepository.save(spacing);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spacing.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /spacings/:id} : Partial updates given fields of an existing spacing, field will ignore if it is null
     *
     * @param id the id of the spacing to save.
     * @param spacing the spacing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spacing,
     * or with status {@code 400 (Bad Request)} if the spacing is not valid,
     * or with status {@code 404 (Not Found)} if the spacing is not found,
     * or with status {@code 500 (Internal Server Error)} if the spacing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spacings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Spacing> partialUpdateSpacing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Spacing spacing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Spacing partially : {}, {}", id, spacing);
        if (spacing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spacing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spacingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Spacing> result = spacingRepository
            .findById(spacing.getId())
            .map(existingSpacing -> {
                if (spacing.getClassName() != null) {
                    existingSpacing.setClassName(spacing.getClassName());
                }

                return existingSpacing;
            })
            .map(spacingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spacing.getId())
        );
    }

    /**
     * {@code GET  /spacings} : get all the spacings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spacings in body.
     */
    @GetMapping("/spacings")
    public List<Spacing> getAllSpacings() {
        log.debug("REST request to get all Spacings");
        return spacingRepository.findAll();
    }

    /**
     * {@code GET  /spacings/:id} : get the "id" spacing.
     *
     * @param id the id of the spacing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spacing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spacings/{id}")
    public ResponseEntity<Spacing> getSpacing(@PathVariable String id) {
        log.debug("REST request to get Spacing : {}", id);
        Optional<Spacing> spacing = spacingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(spacing);
    }

    /**
     * {@code DELETE  /spacings/:id} : delete the "id" spacing.
     *
     * @param id the id of the spacing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spacings/{id}")
    public ResponseEntity<Void> deleteSpacing(@PathVariable String id) {
        log.debug("REST request to delete Spacing : {}", id);
        spacingRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
