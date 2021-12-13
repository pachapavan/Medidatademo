package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.FormWrap;
import com.cpp.dataapi.repository.FormWrapRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.FormWrap}.
 */
@RestController
@RequestMapping("/api")
public class FormWrapResource {

    private final Logger log = LoggerFactory.getLogger(FormWrapResource.class);

    private static final String ENTITY_NAME = "formWrap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormWrapRepository formWrapRepository;

    public FormWrapResource(FormWrapRepository formWrapRepository) {
        this.formWrapRepository = formWrapRepository;
    }

    /**
     * {@code POST  /form-wraps} : Create a new formWrap.
     *
     * @param formWrap the formWrap to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formWrap, or with status {@code 400 (Bad Request)} if the formWrap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-wraps")
    public ResponseEntity<FormWrap> createFormWrap(@RequestBody FormWrap formWrap) throws URISyntaxException {
        log.debug("REST request to save FormWrap : {}", formWrap);
        if (formWrap.getId() != null) {
            throw new BadRequestAlertException("A new formWrap cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormWrap result = formWrapRepository.save(formWrap);
        return ResponseEntity
            .created(new URI("/api/form-wraps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /form-wraps/:id} : Updates an existing formWrap.
     *
     * @param id the id of the formWrap to save.
     * @param formWrap the formWrap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formWrap,
     * or with status {@code 400 (Bad Request)} if the formWrap is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formWrap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-wraps/{id}")
    public ResponseEntity<FormWrap> updateFormWrap(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FormWrap formWrap
    ) throws URISyntaxException {
        log.debug("REST request to update FormWrap : {}, {}", id, formWrap);
        if (formWrap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formWrap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formWrapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormWrap result = formWrapRepository.save(formWrap);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formWrap.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-wraps/:id} : Partial updates given fields of an existing formWrap, field will ignore if it is null
     *
     * @param id the id of the formWrap to save.
     * @param formWrap the formWrap to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formWrap,
     * or with status {@code 400 (Bad Request)} if the formWrap is not valid,
     * or with status {@code 404 (Not Found)} if the formWrap is not found,
     * or with status {@code 500 (Internal Server Error)} if the formWrap couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-wraps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormWrap> partialUpdateFormWrap(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody FormWrap formWrap
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormWrap partially : {}, {}", id, formWrap);
        if (formWrap.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formWrap.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formWrapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormWrap> result = formWrapRepository
            .findById(formWrap.getId())
            .map(existingFormWrap -> {
                return existingFormWrap;
            })
            .map(formWrapRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formWrap.getId())
        );
    }

    /**
     * {@code GET  /form-wraps} : get all the formWraps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formWraps in body.
     */
    @GetMapping("/form-wraps")
    public List<FormWrap> getAllFormWraps() {
        log.debug("REST request to get all FormWraps");
        return formWrapRepository.findAll();
    }

    /**
     * {@code GET  /form-wraps/:id} : get the "id" formWrap.
     *
     * @param id the id of the formWrap to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formWrap, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-wraps/{id}")
    public ResponseEntity<FormWrap> getFormWrap(@PathVariable String id) {
        log.debug("REST request to get FormWrap : {}", id);
        Optional<FormWrap> formWrap = formWrapRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formWrap);
    }

    /**
     * {@code DELETE  /form-wraps/:id} : delete the "id" formWrap.
     *
     * @param id the id of the formWrap to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-wraps/{id}")
    public ResponseEntity<Void> deleteFormWrap(@PathVariable String id) {
        log.debug("REST request to delete FormWrap : {}", id);
        formWrapRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
