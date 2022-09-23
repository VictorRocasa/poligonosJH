package com.mycompany.myapp.service.dto;

import java.util.Objects;

public class EstoquePoligonosDTO {

    private int lados;

    private float tamanho;

    private long ocorrencias;

    public EstoquePoligonosDTO() {}

    public EstoquePoligonosDTO(int lados, float tamanho, long ocorrencias) {
        this.lados = lados;
        this.tamanho = tamanho;
        this.ocorrencias = ocorrencias;
    }

    public int getLados() {
        return this.lados;
    }

    public void setLados(int lados) {
        this.lados = lados;
    }

    public float getTamanho() {
        return this.tamanho;
    }

    public void setTamanho(float tamanho) {
        this.tamanho = tamanho;
    }

    public long getOcorrencias() {
        return this.ocorrencias;
    }

    public void setOcorrencias(long ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EstoquePoligonosDTO)) {
            return false;
        }
        EstoquePoligonosDTO estoquePoligonosDTO = (EstoquePoligonosDTO) o;
        return (
            lados == estoquePoligonosDTO.lados && tamanho == estoquePoligonosDTO.tamanho && ocorrencias == estoquePoligonosDTO.ocorrencias
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(lados, tamanho, ocorrencias);
    }

    @Override
    public String toString() {
        return "{" + " lados='" + getLados() + "'" + ", tamanho='" + getTamanho() + "'" + ", ocorrencias='" + getOcorrencias() + "'" + "}";
    }
}
