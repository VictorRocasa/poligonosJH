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

@Component({
  selector: 'jhi-forma-update',
  templateUrl: './forma-update.component.html',
})
export class FormaUpdateComponent implements OnInit {
  isSaving = false;
  forma: IForma | null = null;
  poligonosEscolhidos: IPoligono | null = null;
  formasEscolhidas: IForma | null = null;

  formasSharedCollection: IForma[] = [];

  editForm: FormaFormGroup = this.formaFormService.createFormaFormGroup();

  constructor(
    protected formaService: FormaService,
    protected formaFormService: FormaFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareForma = (o1: IForma | null, o2: IForma | null): boolean => this.formaService.compareForma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forma }) => {
      this.forma = forma;
      if (forma) {
        this.updateForm(forma);
      }

      this.loadRelationshipsOptions();
    });
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
}
