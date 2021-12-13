package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.DisplayAtt;
import com.cpp.dataapi.repository.DisplayAttRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.DisplayAtt}.
 */
@RestController
@RequestMapping("/api")
public class DisplayAttResource {

    private final Logger log = LoggerFactory.getLogger(DisplayAttResource.class);

    private static final String ENTITY_NAME = "displayAtt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisplayAttRepository displayAttRepository;

    public DisplayAttResource(DisplayAttRepository displayAttRepository) {
        this.displayAttRepository = displayAttRepository;
    }

    /**
     * {@code POST  /display-atts} : Create a new displayAtt.
     *
     * @param displayAtt the displayAtt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new displayAtt, or with status {@code 400 (Bad Request)} if the displayAtt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/display-atts")
    public ResponseEntity<DisplayAtt> createDisplayAtt(@RequestBody DisplayAtt displayAtt) throws URISyntaxException {
        log.debug("REST request to save DisplayAtt : {}", displayAtt);
        if (displayAtt.getId() != null) {
            throw new BadRequestAlertException("A new displayAtt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisplayAtt result = displayAttRepository.save(displayAtt);
        return ResponseEntity
            .created(new URI("/api/display-atts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /display-atts/:id} : Updates an existing displayAtt.
     *
     * @param id the id of the displayAtt to save.
     * @param displayAtt the displayAtt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated displayAtt,
     * or with status {@code 400 (Bad Request)} if the displayAtt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the displayAtt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/display-atts/{id}")
    public ResponseEntity<DisplayAtt> updateDisplayAtt(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DisplayAtt displayAtt
    ) throws URISyntaxException {
        log.debug("REST request to update DisplayAtt : {}, {}", id, displayAtt);
        if (displayAtt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, displayAtt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!displayAttRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisplayAtt result = displayAttRepository.save(displayAtt);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, displayAtt.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /display-atts/:id} : Partial updates given fields of an existing displayAtt, field will ignore if it is null
     *
     * @param id the id of the displayAtt to save.
     * @param displayAtt the displayAtt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated displayAtt,
     * or with status {@code 400 (Bad Request)} if the displayAtt is not valid,
     * or with status {@code 404 (Not Found)} if the displayAtt is not found,
     * or with status {@code 500 (Internal Server Error)} if the displayAtt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/display-atts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisplayAtt> partialUpdateDisplayAtt(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DisplayAtt displayAtt
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisplayAtt partially : {}, {}", id, displayAtt);
        if (displayAtt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, displayAtt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!displayAttRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisplayAtt> result = displayAttRepository
            .findById(displayAtt.getId())
            .map(existingDisplayAtt -> {
                if (displayAtt.getName() != null) {
                    existingDisplayAtt.setName(displayAtt.getName());
                }
                if (displayAtt.getType() != null) {
                    existingDisplayAtt.setType(displayAtt.getType());
                }

                return existingDisplayAtt;
            })
            .map(displayAttRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, displayAtt.getId())
        );
    }

    /**
     * {@code GET  /display-atts} : get all the displayAtts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of displayAtts in body.
     */
    @GetMapping("/display-atts")
    public List<DisplayAtt> getAllDisplayAtts() {
        log.debug("REST request to get all DisplayAtts");
        return displayAttRepository.findAll();
    }

    /**
     * {@code GET  /display-atts/:id} : get the "id" displayAtt.
     *
     * @param id the id of the displayAtt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the displayAtt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/display-atts/{id}")
    public ResponseEntity<DisplayAtt> getDisplayAtt(@PathVariable String id) {
        log.debug("REST request to get DisplayAtt : {}", id);
        Optional<DisplayAtt> displayAtt = displayAttRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(displayAtt);
    }

    /**
     * {@code DELETE  /display-atts/:id} : delete the "id" displayAtt.
     *
     * @param id the id of the displayAtt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/display-atts/{id}")
    public ResponseEntity<Void> deleteDisplayAtt(@PathVariable String id) {
        log.debug("REST request to delete DisplayAtt : {}", id);
        displayAttRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
