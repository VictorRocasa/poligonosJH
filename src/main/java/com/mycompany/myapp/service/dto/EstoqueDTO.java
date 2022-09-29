package com.mycompany.myapp.service.dto;

import java.util.Objects;
import java.util.Set;

public class EstoqueDTO {

    Set<EstoquePoligonosDTO> poligonos;

    Set<FormaDTO> formas;

    public EstoqueDTO() {}

    public EstoqueDTO(Set<EstoquePoligonosDTO> poligonos, Set<FormaDTO> formas) {
        this.poligonos = poligonos;
        this.formas = formas;
    }

    public Set<EstoquePoligonosDTO> getPoligonos() {
        return this.poligonos;
    }

    public void setPoligonos(Set<EstoquePoligonosDTO> poligonos) {
        this.poligonos = poligonos;
    }

    public Set<FormaDTO> getFormas() {
        return this.formas;
    }

    public void setFormas(Set<FormaDTO> formas) {
        this.formas = formas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EstoqueDTO)) {
            return false;
        }
        EstoqueDTO estoqueDTO = (EstoqueDTO) o;
        return Objects.equals(poligonos, estoqueDTO.poligonos) && Objects.equals(formas, estoqueDTO.formas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(poligonos, formas);
    }

    @Override
    public String toString() {
        return "{" + " poligonos='" + getPoligonos() + "'" + ", formas='" + getFormas() + "'" + "}";
    }
}
