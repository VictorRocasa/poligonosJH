package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Forma.
 */
@Entity
@Table(name = "forma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Forma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "forma")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "forma" }, allowSetters = true)
    private Set<Poligono> poligonos = new HashSet<>();

    @OneToMany(mappedBy = "agrupamento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "poligonos", "formas", "agrupamento" }, allowSetters = true)
    private Set<Forma> formas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "poligonos", "formas", "agrupamento" }, allowSetters = true)
    private Forma agrupamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Forma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Poligono> getPoligonos() {
        return this.poligonos;
    }

    public void setPoligonos(Set<Poligono> poligonos) {
        if (this.poligonos != null) {
            this.poligonos.forEach(i -> i.setForma(null));
        }
        if (poligonos != null) {
            poligonos.forEach(i -> i.setForma(this));
        }
        this.poligonos = poligonos;
    }

    public Forma poligonos(Set<Poligono> poligonos) {
        this.setPoligonos(poligonos);
        return this;
    }

    public Forma addPoligono(Poligono poligono) {
        this.poligonos.add(poligono);
        poligono.setForma(this);
        return this;
    }

    public Forma removePoligono(Poligono poligono) {
        this.poligonos.remove(poligono);
        poligono.setForma(null);
        return this;
    }

    public Set<Forma> getFormas() {
        return this.formas;
    }

    public void setFormas(Set<Forma> formas) {
        if (this.formas != null) {
            this.formas.forEach(i -> i.setAgrupamento(null));
        }
        if (formas != null) {
            formas.forEach(i -> i.setAgrupamento(this));
        }
        this.formas = formas;
    }

    public Forma formas(Set<Forma> formas) {
        this.setFormas(formas);
        return this;
    }

    public Forma addForma(Forma forma) {
        this.formas.add(forma);
        forma.setAgrupamento(this);
        return this;
    }

    public Forma removeForma(Forma forma) {
        this.formas.remove(forma);
        forma.setAgrupamento(null);
        return this;
    }

    public Forma getAgrupamento() {
        return this.agrupamento;
    }

    public void setAgrupamento(Forma forma) {
        this.agrupamento = forma;
    }

    public Forma agrupamento(Forma forma) {
        this.setAgrupamento(forma);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forma)) {
            return false;
        }
        return id != null && id.equals(((Forma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forma{" +
            "id=" + getId() +
            "}";
    }
}
