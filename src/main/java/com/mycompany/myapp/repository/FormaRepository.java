package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.service.dto.FormaDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Forma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormaRepository extends JpaRepository<Forma, Long> {
    Page<Forma> findAllByAgrupamento(Forma forma, Pageable pageable);
}
