import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICkEntity, NewCkEntity } from '../ck-entity.model';

export type PartialUpdateCkEntity = Partial<ICkEntity> & Pick<ICkEntity, 'id'>;

export type EntityResponseType = HttpResponse<ICkEntity>;
export type EntityArrayResponseType = HttpResponse<ICkEntity[]>;

@Injectable({ providedIn: 'root' })
export class CkEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ck-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ckEntity: NewCkEntity): Observable<EntityResponseType> {
    return this.http.post<ICkEntity>(this.resourceUrl, ckEntity, { observe: 'response' });
  }

  update(ckEntity: ICkEntity): Observable<EntityResponseType> {
    return this.http.put<ICkEntity>(`${this.resourceUrl}/${this.getCkEntityIdentifier(ckEntity)}`, ckEntity, { observe: 'response' });
  }

  partialUpdate(ckEntity: PartialUpdateCkEntity): Observable<EntityResponseType> {
    return this.http.patch<ICkEntity>(`${this.resourceUrl}/${this.getCkEntityIdentifier(ckEntity)}`, ckEntity, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICkEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICkEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCkEntityIdentifier(ckEntity: Pick<ICkEntity, 'id'>): string {
    return ckEntity.id;
  }

  compareCkEntity(o1: Pick<ICkEntity, 'id'> | null, o2: Pick<ICkEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getCkEntityIdentifier(o1) === this.getCkEntityIdentifier(o2) : o1 === o2;
  }

  addCkEntityToCollectionIfMissing<Type extends Pick<ICkEntity, 'id'>>(
    ckEntityCollection: Type[],
    ...ckEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ckEntities: Type[] = ckEntitiesToCheck.filter(isPresent);
    if (ckEntities.length > 0) {
      const ckEntityCollectionIdentifiers = ckEntityCollection.map(ckEntityItem => this.getCkEntityIdentifier(ckEntityItem)!);
      const ckEntitiesToAdd = ckEntities.filter(ckEntityItem => {
        const ckEntityIdentifier = this.getCkEntityIdentifier(ckEntityItem);
        if (ckEntityCollectionIdentifiers.includes(ckEntityIdentifier)) {
          return false;
        }
        ckEntityCollectionIdentifiers.push(ckEntityIdentifier);
        return true;
      });
      return [...ckEntitiesToAdd, ...ckEntityCollection];
    }
    return ckEntityCollection;
  }
}
