package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.ObjectContainingString;
import com.cpp.dataapi.repository.ObjectContainingStringRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.ObjectContainingString}.
 */
@RestController
@RequestMapping("/api")
public class ObjectContainingStringResource {

    private final Logger log = LoggerFactory.getLogger(ObjectContainingStringResource.class);

    private static final String ENTITY_NAME = "objectContainingString";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectContainingStringRepository objectContainingStringRepository;

    public ObjectContainingStringResource(ObjectContainingStringRepository objectContainingStringRepository) {
        this.objectContainingStringRepository = objectContainingStringRepository;
    }

    /**
     * {@code POST  /object-containing-strings} : Create a new objectContainingString.
     *
     * @param objectContainingString the objectContainingString to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objectContainingString, or with status {@code 400 (Bad Request)} if the objectContainingString has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/object-containing-strings")
    public ResponseEntity<ObjectContainingString> createObjectContainingString(@RequestBody ObjectContainingString objectContainingString)
        throws URISyntaxException {
        log.debug("REST request to save ObjectContainingString : {}", objectContainingString);
        if (objectContainingString.getId() != null) {
            throw new BadRequestAlertException("A new objectContainingString cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ObjectContainingString result = objectContainingStringRepository.save(objectContainingString);
        return ResponseEntity
            .created(new URI("/api/object-containing-strings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /object-containing-strings/:id} : Updates an existing objectContainingString.
     *
     * @param id the id of the objectContainingString to save.
     * @param objectContainingString the objectContainingString to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectContainingString,
     * or with status {@code 400 (Bad Request)} if the objectContainingString is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objectContainingString couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/object-containing-strings/{id}")
    public ResponseEntity<ObjectContainingString> updateObjectContainingString(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ObjectContainingString objectContainingString
    ) throws URISyntaxException {
        log.debug("REST request to update ObjectContainingString : {}, {}", id, objectContainingString);
        if (objectContainingString.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectContainingString.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectContainingStringRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ObjectContainingString result = objectContainingStringRepository.save(objectContainingString);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objectContainingString.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /object-containing-strings/:id} : Partial updates given fields of an existing objectContainingString, field will ignore if it is null
     *
     * @param id the id of the objectContainingString to save.
     * @param objectContainingString the objectContainingString to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectContainingString,
     * or with status {@code 400 (Bad Request)} if the objectContainingString is not valid,
     * or with status {@code 404 (Not Found)} if the objectContainingString is not found,
     * or with status {@code 500 (Internal Server Error)} if the objectContainingString couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/object-containing-strings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ObjectContainingString> partialUpdateObjectContainingString(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ObjectContainingString objectContainingString
    ) throws URISyntaxException {
        log.debug("REST request to partial update ObjectContainingString partially : {}, {}", id, objectContainingString);
        if (objectContainingString.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectContainingString.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objectContainingStringRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ObjectContainingString> result = objectContainingStringRepository
            .findById(objectContainingString.getId())
            .map(existingObjectContainingString -> {
                if (objectContainingString.getName() != null) {
                    existingObjectContainingString.setName(objectContainingString.getName());
                }

                return existingObjectContainingString;
            })
            .map(objectContainingStringRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, objectContainingString.getId())
        );
    }

    /**
     * {@code GET  /object-containing-strings} : get all the objectContainingStrings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objectContainingStrings in body.
     */
    @GetMapping("/object-containing-strings")
    public List<ObjectContainingString> getAllObjectContainingStrings() {
        log.debug("REST request to get all ObjectContainingStrings");
        return objectContainingStringRepository.findAll();
    }

    /**
     * {@code GET  /object-containing-strings/:id} : get the "id" objectContainingString.
     *
     * @param id the id of the objectContainingString to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objectContainingString, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/object-containing-strings/{id}")
    public ResponseEntity<ObjectContainingString> getObjectContainingString(@PathVariable String id) {
        log.debug("REST request to get ObjectContainingString : {}", id);
        Optional<ObjectContainingString> objectContainingString = objectContainingStringRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(objectContainingString);
    }

    /**
     * {@code DELETE  /object-containing-strings/:id} : delete the "id" objectContainingString.
     *
     * @param id the id of the objectContainingString to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/object-containing-strings/{id}")
    public ResponseEntity<Void> deleteObjectContainingString(@PathVariable String id) {
        log.debug("REST request to delete ObjectContainingString : {}", id);
        objectContainingStringRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
