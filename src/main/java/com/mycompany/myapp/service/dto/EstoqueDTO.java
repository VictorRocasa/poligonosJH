package com.mycompany.myapp.service.dto;

import java.util.List;
import java.util.Objects;

public class EstoqueDTO {

    List<EstoquePoligonosDTO> poligonos;

    List<FormaDTO> formas;

    public EstoqueDTO() {}

    public EstoqueDTO(List<EstoquePoligonosDTO> poligonos, List<FormaDTO> formas) {
        this.poligonos = poligonos;
        this.formas = formas;
    }

    public List<EstoquePoligonosDTO> getPoligonos() {
        return this.poligonos;
    }

    public void setPoligonos(List<EstoquePoligonosDTO> poligonos) {
        this.poligonos = poligonos;
    }

    public List<FormaDTO> getFormas() {
        return this.formas;
    }

    public void setFormas(List<FormaDTO> formas) {
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
