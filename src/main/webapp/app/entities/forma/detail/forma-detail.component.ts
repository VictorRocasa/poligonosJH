import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IForma } from '../forma.model';

@Component({
  selector: 'jhi-forma-detail',
  templateUrl: './forma-detail.component.html',
})
export class FormaDetailComponent implements OnInit {
  forma: IForma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forma }) => {
      this.forma = forma;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
