package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Poligono;
import com.mycompany.myapp.service.dto.EstoquePoligonosDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Poligono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoligonoRepository extends JpaRepository<Poligono, Long> {
    @Query(
        "select new com.mycompany.myapp.service.dto.EstoquePoligonosDTO(p.lados,p.tamanho,count(p.lados)) from Poligono p " +
        "group by (p.lados,p.tamanho,p.forma) having p.forma is null"
    )
    Page<EstoquePoligonosDTO> listarEstoque(Pageable pageable);
}
