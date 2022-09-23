package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EstoqueDTO;
import com.mycompany.myapp.service.dto.EstoquePoligonosDTO;
import com.mycompany.myapp.service.dto.FormaDTO;
import com.mycompany.myapp.web.rest.PoligonoResource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstoqueService {

    private final Logger log = LoggerFactory.getLogger(PoligonoResource.class);

    PoligonoService poligonoService;

    FormaService formaService;

    public EstoqueService(PoligonoService poligonoService, FormaService formaService) {
        this.poligonoService = poligonoService;
        this.formaService = formaService;
    }

    public EstoqueDTO findAll(Pageable pageable) {
        log.debug("Requerimento para listar estoque");
        List<EstoquePoligonosDTO> estoquePoligonos = poligonoService.listarEstoque(pageable);
        List<FormaDTO> estoqueFormas = formaService.listarEstoque(pageable);
        return new EstoqueDTO(estoquePoligonos, estoqueFormas);
    }
}