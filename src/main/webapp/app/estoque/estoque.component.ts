import { Component, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IEstoque } from './estoque.model';
import { EstoqueService } from 'app/estoque/service/estoque.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { combineLatest, finalize, Observable, switchMap, tap } from 'rxjs';
import { ASC, DEFAULT_SORT_DATA, DESC, SORT } from 'app/config/navigation.constants';
import { HttpHeaders } from '@angular/common/http';
import { IForma } from 'app/entities/forma/forma.model';
import { IPoligono } from 'app/entities/poligono/poligono.model';
import { FormGroup } from '@angular/forms';
import { IEstoquePoligonos } from './estoquePoligono.model';
@Component({
  selector: 'jhi-estoque',
  templateUrl: './estoque.component.html',
})
export class EstoqueComponent implements OnInit {
  estoque?: IEstoque;
  poligonosEscolhidos!: number[];
  formasEscolhidas!: boolean[];
  iniciado = false;
  isSaving = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  constructor(
    public router: Router,
    protected activatedRoute: ActivatedRoute,
    protected estoqueService: EstoqueService,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.estoqueService.find().subscribe(estoque => (this.estoque = estoque));
  }

  iniciar(): void {
    if (this.estoque) {
      if (this.estoque.poligonos) {
        this.poligonosEscolhidos = new Array(this.estoque.poligonos.length);
        for (let i: number = 0; i < this.estoque.poligonos.length; i++) this.poligonosEscolhidos[i] = 0;
      }
      if (this.estoque.formas) {
        this.formasEscolhidas = new Array(this.estoque.formas.length);
        for (let i: number = 0; i < this.estoque.formas.length; i++) this.formasEscolhidas[i] = false;
      }
    }
    this.iniciado = true;
  }

  selecionaPoligono(index: number, valor: string): void {
    if (!this.iniciado) this.iniciar();
    this.poligonosEscolhidos[index] = Number(valor);
  }

  selecionaForma(index: number): void {
    if (!this.iniciado) this.iniciar();
    if (!this.formasEscolhidas[index]) this.formasEscolhidas[index] = true;
    else this.formasEscolhidas[index] = false;
  }

  enviarObjetos(): void {
    this.isSaving = true;
    let poligonos: IEstoquePoligonos[] = [];
    if (this.poligonosEscolhidos) {
      for (let i = 0; i < this.poligonosEscolhidos.length; i++) {
        if (this.poligonosEscolhidos[i] > 0) {
          let p: IEstoquePoligonos = this.estoque!.poligonos![i];
          p.ocorrencias = this.poligonosEscolhidos[i];
          poligonos.push(p);
        }
      }
    }
    let formas: IForma[] = [];
    if (this.formasEscolhidas) for (let i = 0; i < this.formasEscolhidas.length; i++) formas.push(this.estoque!.formas![i]);
    this.estoqueService.gerarForma({ poligonos, formas } as IEstoque)
      .pipe(finalize(() => this.onSaveFinalize()))
      .subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
  }

  protected onSaveSuccess(): void {
    window.location.reload();
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  getResumoPoligono(poligono: IPoligono): string {
    return poligono.lados + ' lados de tamanho ' + poligono.tamanho;
  }

  getResumoForma(forma: IForma): string {
    let resumo = '';
    if (forma.poligonos)
      if (forma.poligonos.length === 0) resumo += '0 poligonos ';
      else if (forma.poligonos.length == 1) resumo += '1 poligono ';
      else if (forma.poligonos.length > 1) {
        resumo += forma.poligonos.length + ' poligonos ';
        resumo += 'e ';
      }

    if (forma.formas)
      if (forma.formas.length === 0) resumo += '0 formas';
      else if (forma.formas.length == 1) resumo += '1 forma';
      else if (forma.formas.length > 1) resumo += forma.formas.length + ' formas ';
    return forma.id + ': ' + resumo;
  }

  previousState(): void {
    window.history.back();
  }
}
