package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.repository.FormaRepository;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.service.mapper.FormaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FormaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormaResourceIT {

    private static final String ENTITY_API_URL = "/api/formas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormaRepository formaRepository;

    @Autowired
    private FormaMapper formaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormaMockMvc;

    private Forma forma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forma createEntity(EntityManager em) {
        Forma forma = new Forma();
        return forma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forma createUpdatedEntity(EntityManager em) {
        Forma forma = new Forma();
        return forma;
    }

    @BeforeEach
    public void initTest() {
        forma = createEntity(em);
    }

    @Test
    @Transactional
    void createForma() throws Exception {
        int databaseSizeBeforeCreate = formaRepository.findAll().size();
        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);
        restFormaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaDTO)))
            .andExpect(status().isCreated());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeCreate + 1);
        Forma testForma = formaList.get(formaList.size() - 1);
    }

    @Test
    @Transactional
    void createFormaWithExistingId() throws Exception {
        // Create the Forma with an existing ID
        forma.setId(1L);
        FormaDTO formaDTO = formaMapper.toDto(forma);

        int databaseSizeBeforeCreate = formaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormas() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        // Get all the formaList
        restFormaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forma.getId().intValue())));
    }

    @Test
    @Transactional
    void getForma() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        // Get the forma
        restFormaMockMvc
            .perform(get(ENTITY_API_URL_ID, forma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forma.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingForma() throws Exception {
        // Get the forma
        restFormaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingForma() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        int databaseSizeBeforeUpdate = formaRepository.findAll().size();

        // Update the forma
        Forma updatedForma = formaRepository.findById(forma.getId()).get();
        // Disconnect from session so that the updates on updatedForma are not directly saved in db
        em.detach(updatedForma);
        FormaDTO formaDTO = formaMapper.toDto(updatedForma);

        restFormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
        Forma testForma = formaList.get(formaList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormaWithPatch() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        int databaseSizeBeforeUpdate = formaRepository.findAll().size();

        // Update the forma using partial update
        Forma partialUpdatedForma = new Forma();
        partialUpdatedForma.setId(forma.getId());

        restFormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForma))
            )
            .andExpect(status().isOk());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
        Forma testForma = formaList.get(formaList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFormaWithPatch() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        int databaseSizeBeforeUpdate = formaRepository.findAll().size();

        // Update the forma using partial update
        Forma partialUpdatedForma = new Forma();
        partialUpdatedForma.setId(forma.getId());

        restFormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForma))
            )
            .andExpect(status().isOk());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
        Forma testForma = formaList.get(formaList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForma() throws Exception {
        int databaseSizeBeforeUpdate = formaRepository.findAll().size();
        forma.setId(count.incrementAndGet());

        // Create the Forma
        FormaDTO formaDTO = formaMapper.toDto(forma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forma in the database
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForma() throws Exception {
        // Initialize the database
        formaRepository.saveAndFlush(forma);

        int databaseSizeBeforeDelete = formaRepository.findAll().size();

        // Delete the forma
        restFormaMockMvc
            .perform(delete(ENTITY_API_URL_ID, forma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forma> formaList = formaRepository.findAll();
        assertThat(formaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
