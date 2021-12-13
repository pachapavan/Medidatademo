package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Elements;
import com.cpp.dataapi.repository.ElementsRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.Elements}.
 */
@RestController
@RequestMapping("/api")
public class ElementsResource {

    private final Logger log = LoggerFactory.getLogger(ElementsResource.class);

    private static final String ENTITY_NAME = "elements";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElementsRepository elementsRepository;

    public ElementsResource(ElementsRepository elementsRepository) {
        this.elementsRepository = elementsRepository;
    }

    /**
     * {@code POST  /elements} : Create a new elements.
     *
     * @param elements the elements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new elements, or with status {@code 400 (Bad Request)} if the elements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/elements")
    public ResponseEntity<Elements> createElements(@RequestBody Elements elements) throws URISyntaxException {
        log.debug("REST request to save Elements : {}", elements);
        if (elements.getId() != null) {
            throw new BadRequestAlertException("A new elements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Elements result = elementsRepository.save(elements);
        return ResponseEntity
            .created(new URI("/api/elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /elements/:id} : Updates an existing elements.
     *
     * @param id the id of the elements to save.
     * @param elements the elements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elements,
     * or with status {@code 400 (Bad Request)} if the elements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the elements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/elements/{id}")
    public ResponseEntity<Elements> updateElements(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Elements elements
    ) throws URISyntaxException {
        log.debug("REST request to update Elements : {}, {}", id, elements);
        if (elements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Elements result = elementsRepository.save(elements);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, elements.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /elements/:id} : Partial updates given fields of an existing elements, field will ignore if it is null
     *
     * @param id the id of the elements to save.
     * @param elements the elements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elements,
     * or with status {@code 400 (Bad Request)} if the elements is not valid,
     * or with status {@code 404 (Not Found)} if the elements is not found,
     * or with status {@code 500 (Internal Server Error)} if the elements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/elements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Elements> partialUpdateElements(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Elements elements
    ) throws URISyntaxException {
        log.debug("REST request to partial update Elements partially : {}, {}", id, elements);
        if (elements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Elements> result = elementsRepository
            .findById(elements.getId())
            .map(existingElements -> {
                if (elements.getType() != null) {
                    existingElements.setType(elements.getType());
                }

                return existingElements;
            })
            .map(elementsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, elements.getId())
        );
    }

    /**
     * {@code GET  /elements} : get all the elements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elements in body.
     */
    @GetMapping("/elements")
    public List<Elements> getAllElements() {
        log.debug("REST request to get all Elements");
        return elementsRepository.findAll();
    }

    /**
     * {@code GET  /elements/:id} : get the "id" elements.
     *
     * @param id the id of the elements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the elements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/elements/{id}")
    public ResponseEntity<Elements> getElements(@PathVariable String id) {
        log.debug("REST request to get Elements : {}", id);
        Optional<Elements> elements = elementsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(elements);
    }

    /**
     * {@code DELETE  /elements/:id} : delete the "id" elements.
     *
     * @param id the id of the elements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/elements/{id}")
    public ResponseEntity<Void> deleteElements(@PathVariable String id) {
        log.debug("REST request to delete Elements : {}", id);
        elementsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
