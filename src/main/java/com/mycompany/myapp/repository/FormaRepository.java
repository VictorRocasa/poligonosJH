package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Forma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Forma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormaRepository extends JpaRepository<Forma, Long> {}
