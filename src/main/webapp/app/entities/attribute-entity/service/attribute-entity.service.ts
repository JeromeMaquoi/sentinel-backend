import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttributeEntity, NewAttributeEntity } from '../attribute-entity.model';

export type PartialUpdateAttributeEntity = Partial<IAttributeEntity> & Pick<IAttributeEntity, 'id'>;

export type EntityResponseType = HttpResponse<IAttributeEntity>;
export type EntityArrayResponseType = HttpResponse<IAttributeEntity[]>;

@Injectable({ providedIn: 'root' })
export class AttributeEntityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attribute-entities');

  create(attributeEntity: NewAttributeEntity): Observable<EntityResponseType> {
    return this.http.post<IAttributeEntity>(this.resourceUrl, attributeEntity, { observe: 'response' });
  }

  update(attributeEntity: IAttributeEntity): Observable<EntityResponseType> {
    return this.http.put<IAttributeEntity>(`${this.resourceUrl}/${this.getAttributeEntityIdentifier(attributeEntity)}`, attributeEntity, {
      observe: 'response',
    });
  }

  partialUpdate(attributeEntity: PartialUpdateAttributeEntity): Observable<EntityResponseType> {
    return this.http.patch<IAttributeEntity>(`${this.resourceUrl}/${this.getAttributeEntityIdentifier(attributeEntity)}`, attributeEntity, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAttributeEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttributeEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttributeEntityIdentifier(attributeEntity: Pick<IAttributeEntity, 'id'>): string {
    return attributeEntity.id;
  }

  compareAttributeEntity(o1: Pick<IAttributeEntity, 'id'> | null, o2: Pick<IAttributeEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttributeEntityIdentifier(o1) === this.getAttributeEntityIdentifier(o2) : o1 === o2;
  }

  addAttributeEntityToCollectionIfMissing<Type extends Pick<IAttributeEntity, 'id'>>(
    attributeEntityCollection: Type[],
    ...attributeEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attributeEntities: Type[] = attributeEntitiesToCheck.filter(isPresent);
    if (attributeEntities.length > 0) {
      const attributeEntityCollectionIdentifiers = attributeEntityCollection.map(attributeEntityItem =>
        this.getAttributeEntityIdentifier(attributeEntityItem),
      );
      const attributeEntitiesToAdd = attributeEntities.filter(attributeEntityItem => {
        const attributeEntityIdentifier = this.getAttributeEntityIdentifier(attributeEntityItem);
        if (attributeEntityCollectionIdentifiers.includes(attributeEntityIdentifier)) {
          return false;
        }
        attributeEntityCollectionIdentifiers.push(attributeEntityIdentifier);
        return true;
      });
      return [...attributeEntitiesToAdd, ...attributeEntityCollection];
    }
    return attributeEntityCollection;
  }
}
