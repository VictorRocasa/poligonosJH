package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FormaRepository;
import com.mycompany.myapp.service.FormaService;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Forma}.
 */
@RestController
@RequestMapping("/api")
public class FormaResource {

    private final Logger log = LoggerFactory.getLogger(FormaResource.class);

    private static final String ENTITY_NAME = "forma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormaService formaService;

    private final FormaRepository formaRepository;

    public FormaResource(FormaService formaService, FormaRepository formaRepository) {
        this.formaService = formaService;
        this.formaRepository = formaRepository;
    }

    /**
     * {@code POST  /formas} : Create a new forma.
     *
     * @param formaDTO the formaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formaDTO, or with status {@code 400 (Bad Request)} if the forma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formas")
    public ResponseEntity<FormaDTO> createForma(@RequestBody FormaDTO formaDTO) throws URISyntaxException {
        log.debug("REST request to save Forma : {}", formaDTO);
        if (formaDTO.getId() != null) {
            throw new BadRequestAlertException("A new forma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormaDTO result = formaService.save(formaDTO);
        return ResponseEntity
            .created(new URI("/api/formas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formas/:id} : Updates an existing forma.
     *
     * @param id the id of the formaDTO to save.
     * @param formaDTO the formaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaDTO,
     * or with status {@code 400 (Bad Request)} if the formaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formas/{id}")
    public ResponseEntity<FormaDTO> updateForma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormaDTO formaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Forma : {}, {}", id, formaDTO);
        if (formaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormaDTO result = formaService.update(formaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formas/:id} : Partial updates given fields of an existing forma, field will ignore if it is null
     *
     * @param id the id of the formaDTO to save.
     * @param formaDTO the formaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaDTO,
     * or with status {@code 400 (Bad Request)} if the formaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormaDTO> partialUpdateForma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormaDTO formaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Forma partially : {}, {}", id, formaDTO);
        if (formaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormaDTO> result = formaService.partialUpdate(formaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formas} : get all the formas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formas in body.
     */
    @GetMapping("/formas")
    public ResponseEntity<List<FormaDTO>> getAllFormas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Formas");
        Page<FormaDTO> page = formaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formas/:id} : get the "id" forma.
     *
     * @param id the id of the formaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formas/{id}")
    public ResponseEntity<FormaDTO> getForma(@PathVariable Long id) {
        log.debug("REST request to get Forma : {}", id);
        Optional<FormaDTO> formaDTO = formaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formaDTO);
    }

    /**
     * {@code DELETE  /formas/:id} : delete the "id" forma.
     *
     * @param id the id of the formaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formas/{id}")
    public ResponseEntity<Void> deleteForma(@PathVariable Long id) {
        log.debug("REST request to delete Forma : {}", id);
        formaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
