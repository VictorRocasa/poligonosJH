import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PoligonoFormService, PoligonoFormGroup } from './poligono-form.service';
import { IPoligono } from '../poligono.model';
import { PoligonoService } from '../service/poligono.service';
import { IForma } from 'app/entities/forma/forma.model';
import { FormaService } from 'app/entities/forma/service/forma.service';

@Component({
  selector: 'jhi-poligono-update',
  templateUrl: './poligono-update.component.html',
})
export class PoligonoUpdateComponent implements OnInit {
  isSaving = false;
  poligono: IPoligono | null = null;

  formasSharedCollection: IForma[] = [];

  editForm: PoligonoFormGroup = this.poligonoFormService.createPoligonoFormGroup();

  constructor(
    protected poligonoService: PoligonoService,
    protected poligonoFormService: PoligonoFormService,
    protected formaService: FormaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poligono }) => {
      this.poligono = poligono;
      if (poligono) {
        this.updateForm(poligono);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const poligono = this.poligonoFormService.getPoligono(this.editForm);
    if (poligono.id !== null) {
      this.subscribeToSaveResponse(this.poligonoService.update(poligono));
    } else {
      this.subscribeToSaveResponse(this.poligonoService.create(poligono));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoligono>>): void {
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

  protected updateForm(poligono: IPoligono): void {
    this.poligono = poligono;
    this.poligonoFormService.resetForm(this.editForm, poligono);

    this.formasSharedCollection = this.formaService.addFormaToCollectionIfMissing<IForma>(this.formasSharedCollection, poligono.forma);
  }

  protected loadRelationshipsOptions(): void {
    this.formaService
      .query()
      .pipe(map((res: HttpResponse<IForma[]>) => res.body ?? []))
      .pipe(map((formas: IForma[]) => this.formaService.addFormaToCollectionIfMissing<IForma>(formas, this.poligono?.forma)))
      .subscribe((formas: IForma[]) => (this.formasSharedCollection = formas));
  }
}
