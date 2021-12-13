package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Table;
import com.cpp.dataapi.repository.TableRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.Table}.
 */
@RestController
@RequestMapping("/api")
public class TableResource {

    private final Logger log = LoggerFactory.getLogger(TableResource.class);

    private static final String ENTITY_NAME = "table";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TableRepository tableRepository;

    public TableResource(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * {@code POST  /tables} : Create a new table.
     *
     * @param table the table to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new table, or with status {@code 400 (Bad Request)} if the table has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tables")
    public ResponseEntity<Table> createTable(@RequestBody Table table) throws URISyntaxException {
        log.debug("REST request to save Table : {}", table);
        if (table.getId() != null) {
            throw new BadRequestAlertException("A new table cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Table result = tableRepository.save(table);
        return ResponseEntity
            .created(new URI("/api/tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /tables/:id} : Updates an existing table.
     *
     * @param id the id of the table to save.
     * @param table the table to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated table,
     * or with status {@code 400 (Bad Request)} if the table is not valid,
     * or with status {@code 500 (Internal Server Error)} if the table couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tables/{id}")
    public ResponseEntity<Table> updateTable(@PathVariable(value = "id", required = false) final String id, @RequestBody Table table)
        throws URISyntaxException {
        log.debug("REST request to update Table : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Table result = tableRepository.save(table);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, table.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /tables/:id} : Partial updates given fields of an existing table, field will ignore if it is null
     *
     * @param id the id of the table to save.
     * @param table the table to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated table,
     * or with status {@code 400 (Bad Request)} if the table is not valid,
     * or with status {@code 404 (Not Found)} if the table is not found,
     * or with status {@code 500 (Internal Server Error)} if the table couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Table> partialUpdateTable(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Table table
    ) throws URISyntaxException {
        log.debug("REST request to partial update Table partially : {}, {}", id, table);
        if (table.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, table.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Table> result = tableRepository
            .findById(table.getId())
            .map(existingTable -> {
                if (table.getGenericObject() != null) {
                    existingTable.setGenericObject(table.getGenericObject());
                }

                return existingTable;
            })
            .map(tableRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, table.getId()));
    }

    /**
     * {@code GET  /tables} : get all the tables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tables in body.
     */
    @GetMapping("/tables")
    public List<Table> getAllTables() {
        log.debug("REST request to get all Tables");
        return tableRepository.findAll();
    }

    /**
     * {@code GET  /tables/:id} : get the "id" table.
     *
     * @param id the id of the table to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the table, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tables/{id}")
    public ResponseEntity<Table> getTable(@PathVariable String id) {
        log.debug("REST request to get Table : {}", id);
        Optional<Table> table = tableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(table);
    }

    /**
     * {@code DELETE  /tables/:id} : delete the "id" table.
     *
     * @param id the id of the table to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tables/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable String id) {
        log.debug("REST request to delete Table : {}", id);
        tableRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
