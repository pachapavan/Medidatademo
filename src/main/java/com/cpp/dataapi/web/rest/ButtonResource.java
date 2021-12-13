package com.cpp.dataapi.web.rest;

import com.cpp.dataapi.domain.Button;
import com.cpp.dataapi.repository.ButtonRepository;
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
 * REST controller for managing {@link com.cpp.dataapi.domain.Button}.
 */
@RestController
@RequestMapping("/api")
public class ButtonResource {

    private final Logger log = LoggerFactory.getLogger(ButtonResource.class);

    private static final String ENTITY_NAME = "button";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ButtonRepository buttonRepository;

    public ButtonResource(ButtonRepository buttonRepository) {
        this.buttonRepository = buttonRepository;
    }

    /**
     * {@code POST  /buttons} : Create a new button.
     *
     * @param button the button to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new button, or with status {@code 400 (Bad Request)} if the button has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/buttons")
    public ResponseEntity<Button> createButton(@RequestBody Button button) throws URISyntaxException {
        log.debug("REST request to save Button : {}", button);
        if (button.getId() != null) {
            throw new BadRequestAlertException("A new button cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Button result = buttonRepository.save(button);
        return ResponseEntity
            .created(new URI("/api/buttons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /buttons/:id} : Updates an existing button.
     *
     * @param id the id of the button to save.
     * @param button the button to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated button,
     * or with status {@code 400 (Bad Request)} if the button is not valid,
     * or with status {@code 500 (Internal Server Error)} if the button couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/buttons/{id}")
    public ResponseEntity<Button> updateButton(@PathVariable(value = "id", required = false) final String id, @RequestBody Button button)
        throws URISyntaxException {
        log.debug("REST request to update Button : {}, {}", id, button);
        if (button.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, button.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buttonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Button result = buttonRepository.save(button);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, button.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /buttons/:id} : Partial updates given fields of an existing button, field will ignore if it is null
     *
     * @param id the id of the button to save.
     * @param button the button to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated button,
     * or with status {@code 400 (Bad Request)} if the button is not valid,
     * or with status {@code 404 (Not Found)} if the button is not found,
     * or with status {@code 500 (Internal Server Error)} if the button couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/buttons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Button> partialUpdateButton(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Button button
    ) throws URISyntaxException {
        log.debug("REST request to partial update Button partially : {}, {}", id, button);
        if (button.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, button.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!buttonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Button> result = buttonRepository
            .findById(button.getId())
            .map(existingButton -> {
                if (button.getIsLink() != null) {
                    existingButton.setIsLink(button.getIsLink());
                }
                if (button.getLink() != null) {
                    existingButton.setLink(button.getLink());
                }
                if (button.getDisplayText() != null) {
                    existingButton.setDisplayText(button.getDisplayText());
                }
                if (button.getFontSize() != null) {
                    existingButton.setFontSize(button.getFontSize());
                }

                return existingButton;
            })
            .map(buttonRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, button.getId()));
    }

    /**
     * {@code GET  /buttons} : get all the buttons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of buttons in body.
     */
    @GetMapping("/buttons")
    public List<Button> getAllButtons() {
        log.debug("REST request to get all Buttons");
        return buttonRepository.findAll();
    }

    /**
     * {@code GET  /buttons/:id} : get the "id" button.
     *
     * @param id the id of the button to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the button, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/buttons/{id}")
    public ResponseEntity<Button> getButton(@PathVariable String id) {
        log.debug("REST request to get Button : {}", id);
        Optional<Button> button = buttonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(button);
    }

    /**
     * {@code DELETE  /buttons/:id} : delete the "id" button.
     *
     * @param id the id of the button to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/buttons/{id}")
    public ResponseEntity<Void> deleteButton(@PathVariable String id) {
        log.debug("REST request to delete Button : {}", id);
        buttonRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
