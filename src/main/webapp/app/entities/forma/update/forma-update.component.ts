import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FormaFormService, FormaFormGroup } from './forma-form.service';
import { IForma } from '../forma.model';
import { FormaService } from '../service/forma.service';
import { IPoligono } from 'app/entities/poligono/poligono.model';
import { EstoqueComponent } from 'app/estoque/estoque.component';
import { IEstoque } from 'app/estoque/estoque.model';
import { IEstoquePoligonos } from 'app/estoque/estoquePoligono.model';
import { EstoqueService } from 'app/estoque/service/estoque.service';

@Component({
  selector: 'jhi-forma-update',
  templateUrl: './forma-update.component.html',
})
export class FormaUpdateComponent implements OnInit {
  estoque?: IEstoque;
  poligonosEscolhidos!: number[];
  formasEscolhidas!: boolean[];
  isLoading = false;

  isSaving = false;
  forma: IForma | null = null;

  formasSharedCollection: IForma[] = [];

  editForm: FormaFormGroup = this.formaFormService.createFormaFormGroup();

  constructor(
    protected formaService: FormaService,
    protected formaFormService: FormaFormService,
    protected estoqueService: EstoqueService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareForma = (o1: IForma | null, o2: IForma | null): boolean => this.formaService.compareForma(o1, o2);

  async ngOnInit(): Promise<void> {
    this.activatedRoute.data.subscribe(({ forma }) => {
      this.forma = forma;
      if (forma) {
        this.updateForm(forma);
      }

      this.loadRelationshipsOptions();
    });
    await this.forma;
    this.estoqueService.findByForma(this.forma!).subscribe(estoque => (this.estoque = estoque));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const forma = this.formaFormService.getForma(this.editForm);
    if (forma.id !== null) {
      this.subscribeToSaveResponse(this.formaService.update(forma));
    } else {
      this.subscribeToSaveResponse(this.formaService.create(forma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForma>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(forma: IForma): void {
    this.forma = forma;
    this.formaFormService.resetForm(this.editForm, forma);

    this.formasSharedCollection = this.formaService.addFormaToCollectionIfMissing<IForma>(this.formasSharedCollection, forma.agrupamento);
  }

  protected loadRelationshipsOptions(): void {
    this.formaService
      .query()
      .pipe(map((res: HttpResponse<IForma[]>) => res.body ?? []))
      .pipe(map((formas: IForma[]) => this.formaService.addFormaToCollectionIfMissing<IForma>(formas, this.forma?.agrupamento)))
      .subscribe((formas: IForma[]) => (this.formasSharedCollection = formas));
  }

  selecionaPoligono(index: number, valor: string): void {
    this.poligonosEscolhidos[index] = Number(valor);
  }

  selecionaForma(index: number): void {
    if (!this.formasEscolhidas[index]) this.formasEscolhidas[index] = true;
    else this.formasEscolhidas[index] = false;
  }

  enviarObjetos(): void {
    let poligonos: IEstoquePoligonos[] = [];
    for (let i = 0; i < this.poligonosEscolhidos.length; i++) {
      if (this.poligonosEscolhidos[i] > 0) {
        let p: IEstoquePoligonos = this.estoque!.poligonos![i];
        p.ocorrencias = this.poligonosEscolhidos[i];
        poligonos.push(p);
      }
    }
    let formas: IForma[] = [];
    for (let i = 0; i < this.formasEscolhidas.length; i++) {
      if (this.formasEscolhidas) formas.push(this.estoque!.formas![i]);
    }
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
    return resumo;
  }
}
