package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.domain.Poligono;
import com.mycompany.myapp.repository.FormaRepository;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.service.mapper.FormaMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Forma}.
 */
@Service
@Transactional
public class FormaService {

    private final Logger log = LoggerFactory.getLogger(FormaService.class);

    private final FormaRepository formaRepository;

    private final FormaMapper formaMapper;

    public FormaService(FormaRepository formaRepository, FormaMapper formaMapper) {
        this.formaRepository = formaRepository;
        this.formaMapper = formaMapper;
    }

    /**
     * Save a forma.
     *
     * @param formaDTO the entity to save.
     * @return the persisted entity.
     */
    public FormaDTO save(FormaDTO formaDTO) {
        log.debug("Request to save Forma : {}", formaDTO);
        Forma forma = formaMapper.toEntity(formaDTO);
        forma = formaRepository.save(forma);
        return formaMapper.toDto(forma);
    }

    /**
     * Update a forma.
     *
     * @param formaDTO the entity to save.
     * @return the persisted entity.
     */
    public FormaDTO update(FormaDTO formaDTO) {
        log.debug("Request to update Forma : {}", formaDTO);
        Forma forma = formaMapper.toEntity(formaDTO);
        forma = formaRepository.save(forma);
        return formaMapper.toDto(forma);
    }

    /**
     * Partially update a forma.
     *
     * @param formaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FormaDTO> partialUpdate(FormaDTO formaDTO) {
        log.debug("Request to partially update Forma : {}", formaDTO);

        return formaRepository
            .findById(formaDTO.getId())
            .map(existingForma -> {
                formaMapper.partialUpdate(existingForma, formaDTO);

                return existingForma;
            })
            .map(formaRepository::save)
            .map(formaMapper::toDto);
    }

    /**
     * Get all the formas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formas");
        return formaRepository.findAll(pageable).map(formaMapper::toDto);
    }

    /**
     * Get one forma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormaDTO> findOne(Long id) {
        log.debug("Request to get Forma : {}", id);
        return formaRepository.findById(id).map(formaMapper::toDto);
    }

    /**
     * Delete the forma by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Forma : {}", id);
        //formaRepository.deleteBy
        Optional<Forma> forma = formaRepository.findById(id);
        if (forma.isEmpty()) throw new IllegalArgumentException();
        if (forma.get().getAgrupamento() != null) throw new IllegalArgumentException(
            "Impossível deletar uma forma que compõe outra forma!"
        );
        for (Forma f : forma.get().getFormas()) f.setAgrupamento(null);
        for (Poligono p : forma.get().getPoligonos()) p.setForma(null);
        formaRepository.deleteById(id);
    }

    public Set<FormaDTO> listarEstoque(Pageable pageable) {
        log.debug("Requerimento para listar estoque");
        return new HashSet<FormaDTO>(formaRepository.findAllByAgrupamento(null, pageable).map(formaMapper::toDto).getContent());
    }

    public void insereFormasNaForma(Set<Long> idFormas, Forma forma) {
        List<Forma> findAllById = formaRepository.findAllById(idFormas);
        for (Forma f : findAllById) f.setAgrupamento(forma);
    }
}
