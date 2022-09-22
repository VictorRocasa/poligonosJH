package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Forma;
import com.mycompany.myapp.domain.Poligono;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Forma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormaDTO implements Serializable {

    private Long id;

    private FormaDTO agrupamento;

    private Set<Forma> formas;

    private Set<Poligono> poligonos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FormaDTO getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(FormaDTO agrupamento) {
        this.agrupamento = agrupamento;
    }

    public Set<Forma> getFormas() {
        return this.formas;
    }

    public void setFormas(Set<Forma> formas) {
        this.formas = formas;
    }

    public Set<Poligono> getPoligonos() {
        return this.poligonos;
    }

    public void setPoligonos(Set<Poligono> poligonos) {
        this.poligonos = poligonos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormaDTO)) {
            return false;
        }

        FormaDTO formaDTO = (FormaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormaDTO{" +
            "id=" + getId() +
            ", agrupamento=" + getAgrupamento() +
            "}";
    }
}
