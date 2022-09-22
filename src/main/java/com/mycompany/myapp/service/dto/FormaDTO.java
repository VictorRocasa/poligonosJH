package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Forma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormaDTO implements Serializable {

    private Long id;

    private FormaDTO agrupamento;

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
