package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Poligono;
import com.mycompany.myapp.repository.PoligonoRepository;
import com.mycompany.myapp.service.dto.PoligonoDTO;
import com.mycompany.myapp.service.mapper.PoligonoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PoligonoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoligonoResourceIT {

    private static final Integer DEFAULT_LADOS = 1;
    private static final Integer UPDATED_LADOS = 2;

    private static final Float DEFAULT_TAMANHO = 1F;
    private static final Float UPDATED_TAMANHO = 2F;

    private static final Instant DEFAULT_DATA_CRIACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_CRIACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ULTIMA_MODIFICACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ULTIMA_MODIFICACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/poligonos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoligonoRepository poligonoRepository;

    @Autowired
    private PoligonoMapper poligonoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoligonoMockMvc;

    private Poligono poligono;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poligono createEntity(EntityManager em) {
        Poligono poligono = new Poligono()
            .lados(DEFAULT_LADOS)
            .tamanho(DEFAULT_TAMANHO)
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .ultimaModificacao(DEFAULT_ULTIMA_MODIFICACAO);
        return poligono;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poligono createUpdatedEntity(EntityManager em) {
        Poligono poligono = new Poligono()
            .lados(UPDATED_LADOS)
            .tamanho(UPDATED_TAMANHO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);
        return poligono;
    }

    @BeforeEach
    public void initTest() {
        poligono = createEntity(em);
    }

    @Test
    @Transactional
    void createPoligono() throws Exception {
        int databaseSizeBeforeCreate = poligonoRepository.findAll().size();
        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);
        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isCreated());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeCreate + 1);
        Poligono testPoligono = poligonoList.get(poligonoList.size() - 1);
        assertThat(testPoligono.getLados()).isEqualTo(DEFAULT_LADOS);
        assertThat(testPoligono.getTamanho()).isEqualTo(DEFAULT_TAMANHO);
        assertThat(testPoligono.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testPoligono.getUltimaModificacao()).isEqualTo(DEFAULT_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    void createPoligonoWithExistingId() throws Exception {
        // Create the Poligono with an existing ID
        poligono.setId(1L);
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        int databaseSizeBeforeCreate = poligonoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLadosIsRequired() throws Exception {
        int databaseSizeBeforeTest = poligonoRepository.findAll().size();
        // set the field null
        poligono.setLados(null);

        // Create the Poligono, which fails.
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isBadRequest());

        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTamanhoIsRequired() throws Exception {
        int databaseSizeBeforeTest = poligonoRepository.findAll().size();
        // set the field null
        poligono.setTamanho(null);

        // Create the Poligono, which fails.
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isBadRequest());

        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = poligonoRepository.findAll().size();
        // set the field null
        poligono.setDataCriacao(null);

        // Create the Poligono, which fails.
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isBadRequest());

        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUltimaModificacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = poligonoRepository.findAll().size();
        // set the field null
        poligono.setUltimaModificacao(null);

        // Create the Poligono, which fails.
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        restPoligonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isBadRequest());

        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPoligonos() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        // Get all the poligonoList
        restPoligonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poligono.getId().intValue())))
            .andExpect(jsonPath("$.[*].lados").value(hasItem(DEFAULT_LADOS)))
            .andExpect(jsonPath("$.[*].tamanho").value(hasItem(DEFAULT_TAMANHO.doubleValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(DEFAULT_DATA_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].ultimaModificacao").value(hasItem(DEFAULT_ULTIMA_MODIFICACAO.toString())));
    }

    @Test
    @Transactional
    void getPoligono() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        // Get the poligono
        restPoligonoMockMvc
            .perform(get(ENTITY_API_URL_ID, poligono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poligono.getId().intValue()))
            .andExpect(jsonPath("$.lados").value(DEFAULT_LADOS))
            .andExpect(jsonPath("$.tamanho").value(DEFAULT_TAMANHO.doubleValue()))
            .andExpect(jsonPath("$.dataCriacao").value(DEFAULT_DATA_CRIACAO.toString()))
            .andExpect(jsonPath("$.ultimaModificacao").value(DEFAULT_ULTIMA_MODIFICACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPoligono() throws Exception {
        // Get the poligono
        restPoligonoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPoligono() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();

        // Update the poligono
        Poligono updatedPoligono = poligonoRepository.findById(poligono.getId()).get();
        // Disconnect from session so that the updates on updatedPoligono are not directly saved in db
        em.detach(updatedPoligono);
        updatedPoligono
            .lados(UPDATED_LADOS)
            .tamanho(UPDATED_TAMANHO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(updatedPoligono);

        restPoligonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poligonoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
        Poligono testPoligono = poligonoList.get(poligonoList.size() - 1);
        assertThat(testPoligono.getLados()).isEqualTo(UPDATED_LADOS);
        assertThat(testPoligono.getTamanho()).isEqualTo(UPDATED_TAMANHO);
        assertThat(testPoligono.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testPoligono.getUltimaModificacao()).isEqualTo(UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    void putNonExistingPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poligonoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poligonoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoligonoWithPatch() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();

        // Update the poligono using partial update
        Poligono partialUpdatedPoligono = new Poligono();
        partialUpdatedPoligono.setId(poligono.getId());

        partialUpdatedPoligono
            .lados(UPDATED_LADOS)
            .tamanho(UPDATED_TAMANHO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);

        restPoligonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoligono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoligono))
            )
            .andExpect(status().isOk());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
        Poligono testPoligono = poligonoList.get(poligonoList.size() - 1);
        assertThat(testPoligono.getLados()).isEqualTo(UPDATED_LADOS);
        assertThat(testPoligono.getTamanho()).isEqualTo(UPDATED_TAMANHO);
        assertThat(testPoligono.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testPoligono.getUltimaModificacao()).isEqualTo(UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    void fullUpdatePoligonoWithPatch() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();

        // Update the poligono using partial update
        Poligono partialUpdatedPoligono = new Poligono();
        partialUpdatedPoligono.setId(poligono.getId());

        partialUpdatedPoligono
            .lados(UPDATED_LADOS)
            .tamanho(UPDATED_TAMANHO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);

        restPoligonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoligono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoligono))
            )
            .andExpect(status().isOk());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
        Poligono testPoligono = poligonoList.get(poligonoList.size() - 1);
        assertThat(testPoligono.getLados()).isEqualTo(UPDATED_LADOS);
        assertThat(testPoligono.getTamanho()).isEqualTo(UPDATED_TAMANHO);
        assertThat(testPoligono.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testPoligono.getUltimaModificacao()).isEqualTo(UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    void patchNonExistingPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poligonoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoligono() throws Exception {
        int databaseSizeBeforeUpdate = poligonoRepository.findAll().size();
        poligono.setId(count.incrementAndGet());

        // Create the Poligono
        PoligonoDTO poligonoDTO = poligonoMapper.toDto(poligono);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoligonoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poligonoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poligono in the database
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoligono() throws Exception {
        // Initialize the database
        poligonoRepository.saveAndFlush(poligono);

        int databaseSizeBeforeDelete = poligonoRepository.findAll().size();

        // Delete the poligono
        restPoligonoMockMvc
            .perform(delete(ENTITY_API_URL_ID, poligono.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poligono> poligonoList = poligonoRepository.findAll();
        assertThat(poligonoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
