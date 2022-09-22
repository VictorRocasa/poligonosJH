package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Poligono} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PoligonoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer lados;

    @NotNull
    private Float tamanho;

    @NotNull
    private Instant dataCriacao;

    @NotNull
    private Instant ultimaModificacao;

    private FormaDTO forma;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLados() {
        return lados;
    }

    public void setLados(Integer lados) {
        this.lados = lados;
    }

    public Float getTamanho() {
        return tamanho;
    }

    public void setTamanho(Float tamanho) {
        this.tamanho = tamanho;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(Instant ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public FormaDTO getForma() {
        return forma;
    }

    public void setForma(FormaDTO forma) {
        this.forma = forma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoligonoDTO)) {
            return false;
        }

        PoligonoDTO poligonoDTO = (PoligonoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, poligonoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoligonoDTO{" +
            "id=" + getId() +
            ", lados=" + getLados() +
            ", tamanho=" + getTamanho() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", ultimaModificacao='" + getUltimaModificacao() + "'" +
            ", forma=" + getForma() +
            "}";
    }
}
