import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForma, NewForma } from '../forma.model';

export type PartialUpdateForma = Partial<IForma> & Pick<IForma, 'id'>;

export type EntityResponseType = HttpResponse<IForma>;
export type EntityArrayResponseType = HttpResponse<IForma[]>;

@Injectable({ providedIn: 'root' })
export class FormaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(forma: NewForma): Observable<EntityResponseType> {
    return this.http.post<IForma>(this.resourceUrl, forma, { observe: 'response' });
  }

  update(forma: IForma): Observable<EntityResponseType> {
    return this.http.put<IForma>(`${this.resourceUrl}/${this.getFormaIdentifier(forma)}`, forma, { observe: 'response' });
  }

  partialUpdate(forma: PartialUpdateForma): Observable<EntityResponseType> {
    return this.http.patch<IForma>(`${this.resourceUrl}/${this.getFormaIdentifier(forma)}`, forma, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IForma>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IForma[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormaIdentifier(forma: Pick<IForma, 'id'>): number {
    return forma.id;
  }

  compareForma(o1: Pick<IForma, 'id'> | null, o2: Pick<IForma, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormaIdentifier(o1) === this.getFormaIdentifier(o2) : o1 === o2;
  }

  addFormaToCollectionIfMissing<Type extends Pick<IForma, 'id'>>(
    formaCollection: Type[],
    ...formasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formas: Type[] = formasToCheck.filter(isPresent);
    if (formas.length > 0) {
      const formaCollectionIdentifiers = formaCollection.map(formaItem => this.getFormaIdentifier(formaItem)!);
      const formasToAdd = formas.filter(formaItem => {
        const formaIdentifier = this.getFormaIdentifier(formaItem);
        if (formaCollectionIdentifiers.includes(formaIdentifier)) {
          return false;
        }
        formaCollectionIdentifiers.push(formaIdentifier);
        return true;
      });
      return [...formasToAdd, ...formaCollection];
    }
    return formaCollection;
  }
}
