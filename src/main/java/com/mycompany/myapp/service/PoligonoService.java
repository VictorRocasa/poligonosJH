package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.domain.Poligono;
import com.mycompany.myapp.repository.PoligonoRepository;
import com.mycompany.myapp.service.dto.EstoquePoligonosDTO;
import com.mycompany.myapp.service.dto.PoligonoDTO;
import com.mycompany.myapp.service.mapper.PoligonoMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Poligono}.
 */
@Service
@Transactional
public class PoligonoService {

    private final Logger log = LoggerFactory.getLogger(PoligonoService.class);

    private final PoligonoRepository poligonoRepository;

    private final PoligonoMapper poligonoMapper;

    public PoligonoService(PoligonoRepository poligonoRepository, PoligonoMapper poligonoMapper) {
        this.poligonoRepository = poligonoRepository;
        this.poligonoMapper = poligonoMapper;
    }

    /**
     * Save a poligono.
     *
     * @param poligonoDTO the entity to save.
     * @return the persisted entity.
     */
    public PoligonoDTO save(PoligonoDTO poligonoDTO) {
        log.debug("Request to save Poligono : {}", poligonoDTO);
        Poligono poligono = poligonoMapper.toEntity(poligonoDTO);
        poligono = poligonoRepository.save(poligono);
        return poligonoMapper.toDto(poligono);
    }

    /**
     * Update a poligono.
     *
     * @param poligonoDTO the entity to save.
     * @return the persisted entity.
     */
    public PoligonoDTO update(PoligonoDTO poligonoDTO) {
        log.debug("Request to update Poligono : {}", poligonoDTO);
        Poligono poligono = poligonoMapper.toEntity(poligonoDTO);
        poligono = poligonoRepository.save(poligono);
        return poligonoMapper.toDto(poligono);
    }

    /**
     * Partially update a poligono.
     *
     * @param poligonoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PoligonoDTO> partialUpdate(PoligonoDTO poligonoDTO) {
        log.debug("Request to partially update Poligono : {}", poligonoDTO);

        return poligonoRepository
            .findById(poligonoDTO.getId())
            .map(existingPoligono -> {
                poligonoMapper.partialUpdate(existingPoligono, poligonoDTO);

                return existingPoligono;
            })
            .map(poligonoRepository::save)
            .map(poligonoMapper::toDto);
    }

    /**
     * Get all the poligonos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PoligonoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Poligonos");
        return poligonoRepository.findAll(pageable).map(poligonoMapper::toDto);
    }

    /**
     * Get one poligono by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PoligonoDTO> findOne(Long id) {
        log.debug("Request to get Poligono : {}", id);
        return poligonoRepository.findById(id).map(poligonoMapper::toDto);
    }

    /**
     * Delete the poligono by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Poligono : {}", id);
        poligonoRepository.deleteById(id);
    }
    
    public Set<EstoquePoligonosDTO> listarEstoque(Pageable pageable) {
        log.debug("Requerimento para listar estoque de poligonos");
        Page<EstoquePoligonosDTO> page = poligonoRepository.listarEstoque(pageable);
        return new HashSet<EstoquePoligonosDTO>(page.getContent());
    }
    public Set<EstoquePoligonosDTO> listarEstoqueByForma(Forma forma) {
        log.debug("Requerimento para listar estoque de poligonos");
        return poligonoRepository.encontrarPoligonosNaForma(forma);
    }

    public Set<Poligono> getPoligonosToInsert(Set<EstoquePoligonosDTO> poligonos) {
        Set<Poligono> poligonosToInsert = new HashSet<>();
        for (EstoquePoligonosDTO p : poligonos) {
            poligonosToInsert.addAll(poligonoRepository.getPoligonosToInsert(p.getLados(), p.getTamanho(), p.getOcorrencias()));
        }
        return poligonosToInsert;
    }
}
