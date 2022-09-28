import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEstoque } from '../estoque.model';

@Injectable({ providedIn: 'root' })
export class EstoqueService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/estoque');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  //HTTP get
  find(): Observable<IEstoque> {
    return this.http.get<IEstoque>(this.resourceUrl).pipe();
  }

  //HTTP post
  gerarForma(selecao: IEstoque): Observable<IEstoque> {
    return this.http.post<IEstoque>(this.resourceUrl, selecao).pipe();
  }
}
