package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Body;
import com.cpp.dataapi.repository.BodyRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.Body}.
 */
@RestController
@RequestMapping("/api")
public class BodyResource {

    private final Logger log = LoggerFactory.getLogger(BodyResource.class);

    private static final String ENTITY_NAME = "body";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BodyRepository bodyRepository;

    public BodyResource(BodyRepository bodyRepository) {
        this.bodyRepository = bodyRepository;
    }

    /**
     * {@code POST  /bodies} : Create a new body.
     *
     * @param body the body to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new body, or with status {@code 400 (Bad Request)} if the body has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bodies")
    public ResponseEntity<Body> createBody(@RequestBody Body body) throws URISyntaxException {
        log.debug("REST request to save Body : {}", body);
        if (body.getId() != null) {
            throw new BadRequestAlertException("A new body cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Body result = bodyRepository.save(body);
        return ResponseEntity
            .created(new URI("/api/bodies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /bodies/:id} : Updates an existing body.
     *
     * @param id the id of the body to save.
     * @param body the body to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated body,
     * or with status {@code 400 (Bad Request)} if the body is not valid,
     * or with status {@code 500 (Internal Server Error)} if the body couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bodies/{id}")
    public ResponseEntity<Body> updateBody(@PathVariable(value = "id", required = false) final String id, @RequestBody Body body)
        throws URISyntaxException {
        log.debug("REST request to update Body : {}, {}", id, body);
        if (body.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, body.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Body result = bodyRepository.save(body);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, body.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /bodies/:id} : Partial updates given fields of an existing body, field will ignore if it is null
     *
     * @param id the id of the body to save.
     * @param body the body to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated body,
     * or with status {@code 400 (Bad Request)} if the body is not valid,
     * or with status {@code 404 (Not Found)} if the body is not found,
     * or with status {@code 500 (Internal Server Error)} if the body couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bodies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Body> partialUpdateBody(@PathVariable(value = "id", required = false) final String id, @RequestBody Body body)
        throws URISyntaxException {
        log.debug("REST request to partial update Body partially : {}, {}", id, body);
        if (body.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, body.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bodyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Body> result = bodyRepository
            .findById(body.getId())
            .map(existingBody -> {
                return existingBody;
            })
            .map(bodyRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, body.getId()));
    }

    /**
     * {@code GET  /bodies} : get all the bodies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bodies in body.
     */
    @GetMapping("/bodies")
    public List<Body> getAllBodies() {
        log.debug("REST request to get all Bodies");
        return bodyRepository.findAll();
    }

    /**
     * {@code GET  /bodies/:id} : get the "id" body.
     *
     * @param id the id of the body to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the body, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bodies/{id}")
    public ResponseEntity<Body> getBody(@PathVariable String id) {
        log.debug("REST request to get Body : {}", id);
        Optional<Body> body = bodyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(body);
    }

    /**
     * {@code DELETE  /bodies/:id} : delete the "id" body.
     *
     * @param id the id of the body to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bodies/{id}")
    public ResponseEntity<Void> deleteBody(@PathVariable String id) {
        log.debug("REST request to delete Body : {}", id);
        bodyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
