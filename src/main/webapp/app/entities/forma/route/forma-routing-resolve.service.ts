import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IForma } from '../forma.model';
import { FormaService } from '../service/forma.service';

@Injectable({ providedIn: 'root' })
export class FormaRoutingResolveService implements Resolve<IForma | null> {
  constructor(protected service: FormaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForma | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((forma: HttpResponse<IForma>) => {
          if (forma.body) {
            return of(forma.body);
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
