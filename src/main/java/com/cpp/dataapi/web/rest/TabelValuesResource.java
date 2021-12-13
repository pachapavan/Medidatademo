package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.TabelValues;
import com.cpp.dataapi.repository.TabelValuesRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.TabelValues}.
 */
@RestController
@RequestMapping("/api")
public class TabelValuesResource {

    private final Logger log = LoggerFactory.getLogger(TabelValuesResource.class);

    private static final String ENTITY_NAME = "tabelValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TabelValuesRepository tabelValuesRepository;

    public TabelValuesResource(TabelValuesRepository tabelValuesRepository) {
        this.tabelValuesRepository = tabelValuesRepository;
    }

    /**
     * {@code POST  /tabel-values} : Create a new tabelValues.
     *
     * @param tabelValues the tabelValues to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tabelValues, or with status {@code 400 (Bad Request)} if the tabelValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tabel-values")
    public ResponseEntity<TabelValues> createTabelValues(@RequestBody TabelValues tabelValues) throws URISyntaxException {
        log.debug("REST request to save TabelValues : {}", tabelValues);
        if (tabelValues.getId() != null) {
            throw new BadRequestAlertException("A new tabelValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TabelValues result = tabelValuesRepository.save(tabelValues);
        return ResponseEntity
            .created(new URI("/api/tabel-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /tabel-values/:id} : Updates an existing tabelValues.
     *
     * @param id the id of the tabelValues to save.
     * @param tabelValues the tabelValues to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelValues,
     * or with status {@code 400 (Bad Request)} if the tabelValues is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tabelValues couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tabel-values/{id}")
    public ResponseEntity<TabelValues> updateTabelValues(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TabelValues tabelValues
    ) throws URISyntaxException {
        log.debug("REST request to update TabelValues : {}, {}", id, tabelValues);
        if (tabelValues.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelValues.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TabelValues result = tabelValuesRepository.save(tabelValues);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tabelValues.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /tabel-values/:id} : Partial updates given fields of an existing tabelValues, field will ignore if it is null
     *
     * @param id the id of the tabelValues to save.
     * @param tabelValues the tabelValues to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabelValues,
     * or with status {@code 400 (Bad Request)} if the tabelValues is not valid,
     * or with status {@code 404 (Not Found)} if the tabelValues is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabelValues couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tabel-values/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TabelValues> partialUpdateTabelValues(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TabelValues tabelValues
    ) throws URISyntaxException {
        log.debug("REST request to partial update TabelValues partially : {}, {}", id, tabelValues);
        if (tabelValues.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tabelValues.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tabelValuesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TabelValues> result = tabelValuesRepository
            .findById(tabelValues.getId())
            .map(existingTabelValues -> {
                return existingTabelValues;
            })
            .map(tabelValuesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tabelValues.getId())
        );
    }

    /**
     * {@code GET  /tabel-values} : get all the tabelValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tabelValues in body.
     */
    @GetMapping("/tabel-values")
    public List<TabelValues> getAllTabelValues() {
        log.debug("REST request to get all TabelValues");
        return tabelValuesRepository.findAll();
    }

    /**
     * {@code GET  /tabel-values/:id} : get the "id" tabelValues.
     *
     * @param id the id of the tabelValues to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tabelValues, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tabel-values/{id}")
    public ResponseEntity<TabelValues> getTabelValues(@PathVariable String id) {
        log.debug("REST request to get TabelValues : {}", id);
        Optional<TabelValues> tabelValues = tabelValuesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tabelValues);
    }

    /**
     * {@code DELETE  /tabel-values/:id} : delete the "id" tabelValues.
     *
     * @param id the id of the tabelValues to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tabel-values/{id}")
    public ResponseEntity<Void> deleteTabelValues(@PathVariable String id) {
        log.debug("REST request to delete TabelValues : {}", id);
        tabelValuesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
