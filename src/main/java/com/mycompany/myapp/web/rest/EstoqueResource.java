package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.EstoqueService;
import com.mycompany.myapp.service.dto.EstoqueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueResource {

    private final Logger log = LoggerFactory.getLogger(FormaResource.class);

    private final EstoqueService estoqueService;

    public EstoqueResource(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public EstoqueDTO getAll(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of estoque");
        return estoqueService.findAll(pageable);
    }

    @PostMapping
    public void gerarForma(@RequestBody EstoqueDTO estoqueDTO) throws IllegalStateException {
        log.debug("Post request to create a Formas");
        if (estoqueDTO.getFormas().isEmpty() && estoqueDTO.getPoligonos().isEmpty()) throw new IllegalStateException(
            "A nova forma não pode ser vazia!"
        );
        estoqueService.gerarForma(estoqueDTO);
    }
}
