package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.FlexBox;
import com.cpp.dataapi.repository.FlexBoxRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.FlexBox}.
 */
@RestController
@RequestMapping("/api")
public class FlexBoxResource {

    private final Logger log = LoggerFactory.getLogger(FlexBoxResource.class);

    private static final String ENTITY_NAME = "flexBox";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexBoxRepository flexBoxRepository;

    public FlexBoxResource(FlexBoxRepository flexBoxRepository) {
        this.flexBoxRepository = flexBoxRepository;
    }

    /**
     * {@code POST  /flex-boxes} : Create a new flexBox.
     *
     * @param flexBox the flexBox to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexBox, or with status {@code 400 (Bad Request)} if the flexBox has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-boxes")
    public ResponseEntity<FlexBox> createFlexBox(@RequestBody FlexBox flexBox) throws URISyntaxException {
        log.debug("REST request to save FlexBox : {}", flexBox);
        if (flexBox.getId() != null) {
            throw new BadRequestAlertException("A new flexBox cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlexBox result = flexBoxRepository.save(flexBox);
        return ResponseEntity
            .created(new URI("/api/flex-boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-boxes/:id} : Updates an existing flexBox.
     *
     * @param id the id of the flexBox to save.
     * @param flexBox the flexBox to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexBox,
     * or with status {@code 400 (Bad Request)} if the flexBox is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexBox couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-boxes/{id}")
    public ResponseEntity<FlexBox> updateFlexBox(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FlexBox flexBox
    ) throws URISyntaxException {
        log.debug("REST request to update FlexBox : {}, {}", id, flexBox);
        if (flexBox.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexBox.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexBoxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FlexBox result = flexBoxRepository.save(flexBox);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexBox.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /flex-boxes/:id} : Partial updates given fields of an existing flexBox, field will ignore if it is null
     *
     * @param id the id of the flexBox to save.
     * @param flexBox the flexBox to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexBox,
     * or with status {@code 400 (Bad Request)} if the flexBox is not valid,
     * or with status {@code 404 (Not Found)} if the flexBox is not found,
     * or with status {@code 500 (Internal Server Error)} if the flexBox couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flex-boxes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlexBox> partialUpdateFlexBox(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FlexBox flexBox
    ) throws URISyntaxException {
        log.debug("REST request to partial update FlexBox partially : {}, {}", id, flexBox);
        if (flexBox.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flexBox.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flexBoxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlexBox> result = flexBoxRepository
            .findById(flexBox.getId())
            .map(existingFlexBox -> {
                if (flexBox.getJustifyContent() != null) {
                    existingFlexBox.setJustifyContent(flexBox.getJustifyContent());
                }

                return existingFlexBox;
            })
            .map(flexBoxRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flexBox.getId())
        );
    }

    /**
     * {@code GET  /flex-boxes} : get all the flexBoxes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexBoxes in body.
     */
    @GetMapping("/flex-boxes")
    public List<FlexBox> getAllFlexBoxes() {
        log.debug("REST request to get all FlexBoxes");
        return flexBoxRepository.findAll();
    }

    /**
     * {@code GET  /flex-boxes/:id} : get the "id" flexBox.
     *
     * @param id the id of the flexBox to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexBox, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-boxes/{id}")
    public ResponseEntity<FlexBox> getFlexBox(@PathVariable String id) {
        log.debug("REST request to get FlexBox : {}", id);
        Optional<FlexBox> flexBox = flexBoxRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(flexBox);
    }

    /**
     * {@code DELETE  /flex-boxes/:id} : delete the "id" flexBox.
     *
     * @param id the id of the flexBox to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-boxes/{id}")
    public ResponseEntity<Void> deleteFlexBox(@PathVariable String id) {
        log.debug("REST request to delete FlexBox : {}", id);
        flexBoxRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
