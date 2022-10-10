package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.dto.EstoquePoligonosDTO;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.service.mapper.FormaMapper;
import com.mycompany.myapp.web.rest.PoligonoResource;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstoqueService {

    private final Logger log = LoggerFactory.getLogger(PoligonoResource.class);

    private final PoligonoService poligonoService;

    private final FormaService formaService;

    private final FormaMapper formaMapper;

    public EstoqueService(PoligonoService poligonoService, FormaService formaService, FormaMapper formaMapper) {
        this.poligonoService = poligonoService;
        this.formaService = formaService;
        this.formaMapper = formaMapper;
    }

    public EstoqueDTO findAll(Pageable pageable) {
        log.debug("Requerimento para listar estoque");
        Set<EstoquePoligonosDTO> estoquePoligonos = poligonoService.listarEstoque(pageable);
        Set<FormaDTO> estoqueFormas = formaService.listarEstoque(pageable);
        return new EstoqueDTO(estoquePoligonos, estoqueFormas);
    }

    @Transactional
    public EstoqueDTO findAllByForma(FormaDTO formaDTO){
        log.debug("Requerimento para listar estoque disponível para uma forma específica");
        Set<EstoquePoligonosDTO> estoquePoligonos = poligonoService.listarEstoqueByForma(formaMapper.toEntity(formaDTO));
        Set<FormaDTO> estoqueFormas = formaService.listarEstoqueByForma(formaMapper.toEntity(formaDTO));
        return new EstoqueDTO(estoquePoligonos, estoqueFormas);
    }

    @Transactional
    public void gerarForma(EstoqueDTO estoqueDTO) {
        FormaDTO formaDTO = new FormaDTO();
        if (estoqueDTO.getPoligonos().size() == 0) formaDTO.setPoligonos(null); else formaDTO.setPoligonos(
            poligonoService.getPoligonosToInsert(estoqueDTO.getPoligonos())
        );
        formaDTO = formaService.save(formaDTO);
        if (estoqueDTO.getFormas().size() != 0) {
            Set<Long> idFormas = new HashSet<>();
            for (FormaDTO forma : estoqueDTO.getFormas()) idFormas.add(forma.getId());
            formaService.insereFormasNaForma(idFormas, formaMapper.toEntity(formaDTO));
        }
    }
}
