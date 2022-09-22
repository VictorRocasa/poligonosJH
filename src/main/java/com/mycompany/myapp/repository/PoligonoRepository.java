package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Poligono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Poligono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoligonoRepository extends JpaRepository<Poligono, Long> {}
