package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Poligono.
 */
@Entity
@Table(name = "poligono")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Poligono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lados", nullable = false)
    private Integer lados;

    @NotNull
    @Column(name = "tamanho", nullable = false)
    private Float tamanho;

    @NotNull
    @Column(name = "data_criacao", nullable = false)
    private Instant dataCriacao;

    @NotNull
    @Column(name = "ultima_modificacao", nullable = false)
    private Instant ultimaModificacao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "poligonos", "formas", "agrupamento" }, allowSetters = true)
    private Forma forma;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Poligono id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLados() {
        return this.lados;
    }

    public Poligono lados(Integer lados) {
        this.setLados(lados);
        return this;
    }

    public void setLados(Integer lados) {
        this.lados = lados;
    }

    public Float getTamanho() {
        return this.tamanho;
    }

    public Poligono tamanho(Float tamanho) {
        this.setTamanho(tamanho);
        return this;
    }

    public void setTamanho(Float tamanho) {
        this.tamanho = tamanho;
    }

    public Instant getDataCriacao() {
        return this.dataCriacao;
    }

    public Poligono dataCriacao(Instant dataCriacao) {
        this.setDataCriacao(dataCriacao);
        return this;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Instant getUltimaModificacao() {
        return this.ultimaModificacao;
    }

    public Poligono ultimaModificacao(Instant ultimaModificacao) {
        this.setUltimaModificacao(ultimaModificacao);
        return this;
    }

    public void setUltimaModificacao(Instant ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public Forma getForma() {
        return this.forma;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
    }

    public Poligono forma(Forma forma) {
        this.setForma(forma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poligono)) {
            return false;
        }
        return id != null && id.equals(((Poligono) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poligono{" +
            "id=" + getId() +
            ", lados=" + getLados() +
            ", tamanho=" + getTamanho() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", ultimaModificacao='" + getUltimaModificacao() + "'" +
            "}";
    }
}
