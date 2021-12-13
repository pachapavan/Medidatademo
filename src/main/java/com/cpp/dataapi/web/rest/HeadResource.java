package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Head;
import com.cpp.dataapi.repository.HeadRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.Head}.
 */
@RestController
@RequestMapping("/api")
public class HeadResource {

    private final Logger log = LoggerFactory.getLogger(HeadResource.class);

    private static final String ENTITY_NAME = "head";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HeadRepository headRepository;

    public HeadResource(HeadRepository headRepository) {
        this.headRepository = headRepository;
    }

    /**
     * {@code POST  /heads} : Create a new head.
     *
     * @param head the head to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new head, or with status {@code 400 (Bad Request)} if the head has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/heads")
    public ResponseEntity<Head> createHead(@RequestBody Head head) throws URISyntaxException {
        log.debug("REST request to save Head : {}", head);
        if (head.getId() != null) {
            throw new BadRequestAlertException("A new head cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Head result = headRepository.save(head);
        return ResponseEntity
            .created(new URI("/api/heads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /heads/:id} : Updates an existing head.
     *
     * @param id the id of the head to save.
     * @param head the head to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated head,
     * or with status {@code 400 (Bad Request)} if the head is not valid,
     * or with status {@code 500 (Internal Server Error)} if the head couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/heads/{id}")
    public ResponseEntity<Head> updateHead(@PathVariable(value = "id", required = false) final String id, @RequestBody Head head)
        throws URISyntaxException {
        log.debug("REST request to update Head : {}, {}", id, head);
        if (head.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, head.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!headRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Head result = headRepository.save(head);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, head.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /heads/:id} : Partial updates given fields of an existing head, field will ignore if it is null
     *
     * @param id the id of the head to save.
     * @param head the head to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated head,
     * or with status {@code 400 (Bad Request)} if the head is not valid,
     * or with status {@code 404 (Not Found)} if the head is not found,
     * or with status {@code 500 (Internal Server Error)} if the head couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/heads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Head> partialUpdateHead(@PathVariable(value = "id", required = false) final String id, @RequestBody Head head)
        throws URISyntaxException {
        log.debug("REST request to partial update Head partially : {}, {}", id, head);
        if (head.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, head.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!headRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Head> result = headRepository
            .findById(head.getId())
            .map(existingHead -> {
                return existingHead;
            })
            .map(headRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, head.getId()));
    }

    /**
     * {@code GET  /heads} : get all the heads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of heads in body.
     */
    @GetMapping("/heads")
    public List<Head> getAllHeads() {
        log.debug("REST request to get all Heads");
        return headRepository.findAll();
    }

    /**
     * {@code GET  /heads/:id} : get the "id" head.
     *
     * @param id the id of the head to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the head, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/heads/{id}")
    public ResponseEntity<Head> getHead(@PathVariable String id) {
        log.debug("REST request to get Head : {}", id);
        Optional<Head> head = headRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(head);
    }

    /**
     * {@code DELETE  /heads/:id} : delete the "id" head.
     *
     * @param id the id of the head to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/heads/{id}")
    public ResponseEntity<Void> deleteHead(@PathVariable String id) {
        log.debug("REST request to delete Head : {}", id);
        headRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
