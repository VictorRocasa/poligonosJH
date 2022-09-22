import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPoligono, NewPoligono } from '../poligono.model';

export type PartialUpdatePoligono = Partial<IPoligono> & Pick<IPoligono, 'id'>;

type RestOf<T extends IPoligono | NewPoligono> = Omit<T, 'dataCriacao' | 'ultimaModificacao'> & {
  dataCriacao?: string | null;
  ultimaModificacao?: string | null;
};

export type RestPoligono = RestOf<IPoligono>;

export type NewRestPoligono = RestOf<NewPoligono>;

export type PartialUpdateRestPoligono = RestOf<PartialUpdatePoligono>;

export type EntityResponseType = HttpResponse<IPoligono>;
export type EntityArrayResponseType = HttpResponse<IPoligono[]>;

@Injectable({ providedIn: 'root' })
export class PoligonoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/poligonos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(poligono: NewPoligono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(poligono);
    return this.http
      .post<RestPoligono>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(poligono: IPoligono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(poligono);
    return this.http
      .put<RestPoligono>(`${this.resourceUrl}/${this.getPoligonoIdentifier(poligono)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(poligono: PartialUpdatePoligono): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(poligono);
    return this.http
      .patch<RestPoligono>(`${this.resourceUrl}/${this.getPoligonoIdentifier(poligono)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPoligono>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPoligono[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPoligonoIdentifier(poligono: Pick<IPoligono, 'id'>): number {
    return poligono.id;
  }

  comparePoligono(o1: Pick<IPoligono, 'id'> | null, o2: Pick<IPoligono, 'id'> | null): boolean {
    return o1 && o2 ? this.getPoligonoIdentifier(o1) === this.getPoligonoIdentifier(o2) : o1 === o2;
  }

  addPoligonoToCollectionIfMissing<Type extends Pick<IPoligono, 'id'>>(
    poligonoCollection: Type[],
    ...poligonosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const poligonos: Type[] = poligonosToCheck.filter(isPresent);
    if (poligonos.length > 0) {
      const poligonoCollectionIdentifiers = poligonoCollection.map(poligonoItem => this.getPoligonoIdentifier(poligonoItem)!);
      const poligonosToAdd = poligonos.filter(poligonoItem => {
        const poligonoIdentifier = this.getPoligonoIdentifier(poligonoItem);
        if (poligonoCollectionIdentifiers.includes(poligonoIdentifier)) {
          return false;
        }
        poligonoCollectionIdentifiers.push(poligonoIdentifier);
        return true;
      });
      return [...poligonosToAdd, ...poligonoCollection];
    }
    return poligonoCollection;
  }

  protected convertDateFromClient<T extends IPoligono | NewPoligono | PartialUpdatePoligono>(poligono: T): RestOf<T> {
    return {
      ...poligono,
      dataCriacao: poligono.dataCriacao?.toJSON() ?? null,
      ultimaModificacao: poligono.ultimaModificacao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPoligono: RestPoligono): IPoligono {
    return {
      ...restPoligono,
      dataCriacao: restPoligono.dataCriacao ? dayjs(restPoligono.dataCriacao) : undefined,
      ultimaModificacao: restPoligono.ultimaModificacao ? dayjs(restPoligono.ultimaModificacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPoligono>): HttpResponse<IPoligono> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPoligono[]>): HttpResponse<IPoligono[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
