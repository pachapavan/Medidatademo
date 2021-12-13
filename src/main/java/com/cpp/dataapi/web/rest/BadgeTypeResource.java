package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.BadgeType;
import com.cpp.dataapi.repository.BadgeTypeRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.BadgeType}.
 */
@RestController
@RequestMapping("/api")
public class BadgeTypeResource {

    private final Logger log = LoggerFactory.getLogger(BadgeTypeResource.class);

    private static final String ENTITY_NAME = "badgeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BadgeTypeRepository badgeTypeRepository;

    public BadgeTypeResource(BadgeTypeRepository badgeTypeRepository) {
        this.badgeTypeRepository = badgeTypeRepository;
    }

    /**
     * {@code POST  /badge-types} : Create a new badgeType.
     *
     * @param badgeType the badgeType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new badgeType, or with status {@code 400 (Bad Request)} if the badgeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/badge-types")
    public ResponseEntity<BadgeType> createBadgeType(@RequestBody BadgeType badgeType) throws URISyntaxException {
        log.debug("REST request to save BadgeType : {}", badgeType);
        if (badgeType.getId() != null) {
            throw new BadRequestAlertException("A new badgeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BadgeType result = badgeTypeRepository.save(badgeType);
        return ResponseEntity
            .created(new URI("/api/badge-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /badge-types/:id} : Updates an existing badgeType.
     *
     * @param id the id of the badgeType to save.
     * @param badgeType the badgeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated badgeType,
     * or with status {@code 400 (Bad Request)} if the badgeType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the badgeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/badge-types/{id}")
    public ResponseEntity<BadgeType> updateBadgeType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BadgeType badgeType
    ) throws URISyntaxException {
        log.debug("REST request to update BadgeType : {}, {}", id, badgeType);
        if (badgeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, badgeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!badgeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BadgeType result = badgeTypeRepository.save(badgeType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, badgeType.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /badge-types/:id} : Partial updates given fields of an existing badgeType, field will ignore if it is null
     *
     * @param id the id of the badgeType to save.
     * @param badgeType the badgeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated badgeType,
     * or with status {@code 400 (Bad Request)} if the badgeType is not valid,
     * or with status {@code 404 (Not Found)} if the badgeType is not found,
     * or with status {@code 500 (Internal Server Error)} if the badgeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/badge-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BadgeType> partialUpdateBadgeType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BadgeType badgeType
    ) throws URISyntaxException {
        log.debug("REST request to partial update BadgeType partially : {}, {}", id, badgeType);
        if (badgeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, badgeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!badgeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BadgeType> result = badgeTypeRepository
            .findById(badgeType.getId())
            .map(existingBadgeType -> {
                if (badgeType.getStatus() != null) {
                    existingBadgeType.setStatus(badgeType.getStatus());
                }
                if (badgeType.getType() != null) {
                    existingBadgeType.setType(badgeType.getType());
                }

                return existingBadgeType;
            })
            .map(badgeTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, badgeType.getId())
        );
    }

    /**
     * {@code GET  /badge-types} : get all the badgeTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of badgeTypes in body.
     */
    @GetMapping("/badge-types")
    public List<BadgeType> getAllBadgeTypes() {
        log.debug("REST request to get all BadgeTypes");
        return badgeTypeRepository.findAll();
    }

    /**
     * {@code GET  /badge-types/:id} : get the "id" badgeType.
     *
     * @param id the id of the badgeType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the badgeType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/badge-types/{id}")
    public ResponseEntity<BadgeType> getBadgeType(@PathVariable String id) {
        log.debug("REST request to get BadgeType : {}", id);
        Optional<BadgeType> badgeType = badgeTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(badgeType);
    }

    /**
     * {@code DELETE  /badge-types/:id} : delete the "id" badgeType.
     *
     * @param id the id of the badgeType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/badge-types/{id}")
    public ResponseEntity<Void> deleteBadgeType(@PathVariable String id) {
        log.debug("REST request to delete BadgeType : {}", id);
        badgeTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
