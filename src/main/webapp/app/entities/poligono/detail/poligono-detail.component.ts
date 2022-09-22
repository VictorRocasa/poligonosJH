import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPoligono } from '../poligono.model';

@Component({
  selector: 'jhi-poligono-detail',
  templateUrl: './poligono-detail.component.html',
})
export class PoligonoDetailComponent implements OnInit {
  poligono: IPoligono | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poligono }) => {
      this.poligono = poligono;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
