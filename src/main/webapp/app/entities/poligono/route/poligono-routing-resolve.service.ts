import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPoligono } from '../poligono.model';
import { PoligonoService } from '../service/poligono.service';

@Injectable({ providedIn: 'root' })
export class PoligonoRoutingResolveService implements Resolve<IPoligono | null> {
  constructor(protected service: PoligonoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPoligono | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((poligono: HttpResponse<IPoligono>) => {
          if (poligono.body) {
            return of(poligono.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
