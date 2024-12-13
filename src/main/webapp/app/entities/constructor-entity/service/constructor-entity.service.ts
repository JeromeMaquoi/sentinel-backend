import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConstructorEntity, NewConstructorEntity } from '../constructor-entity.model';

export type PartialUpdateConstructorEntity = Partial<IConstructorEntity> & Pick<IConstructorEntity, 'id'>;

export type EntityResponseType = HttpResponse<IConstructorEntity>;
export type EntityArrayResponseType = HttpResponse<IConstructorEntity[]>;

@Injectable({ providedIn: 'root' })
export class ConstructorEntityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/constructor-entities');

  create(constructorEntity: NewConstructorEntity): Observable<EntityResponseType> {
    return this.http.post<IConstructorEntity>(this.resourceUrl, constructorEntity, { observe: 'response' });
  }

  update(constructorEntity: IConstructorEntity): Observable<EntityResponseType> {
    return this.http.put<IConstructorEntity>(
      `${this.resourceUrl}/${this.getConstructorEntityIdentifier(constructorEntity)}`,
      constructorEntity,
      { observe: 'response' },
    );
  }

  partialUpdate(constructorEntity: PartialUpdateConstructorEntity): Observable<EntityResponseType> {
    return this.http.patch<IConstructorEntity>(
      `${this.resourceUrl}/${this.getConstructorEntityIdentifier(constructorEntity)}`,
      constructorEntity,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IConstructorEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConstructorEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConstructorEntityIdentifier(constructorEntity: Pick<IConstructorEntity, 'id'>): string {
    return constructorEntity.id;
  }

  compareConstructorEntity(o1: Pick<IConstructorEntity, 'id'> | null, o2: Pick<IConstructorEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getConstructorEntityIdentifier(o1) === this.getConstructorEntityIdentifier(o2) : o1 === o2;
  }

  addConstructorEntityToCollectionIfMissing<Type extends Pick<IConstructorEntity, 'id'>>(
    constructorEntityCollection: Type[],
    ...constructorEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const constructorEntities: Type[] = constructorEntitiesToCheck.filter(isPresent);
    if (constructorEntities.length > 0) {
      const constructorEntityCollectionIdentifiers = constructorEntityCollection.map(constructorEntityItem =>
        this.getConstructorEntityIdentifier(constructorEntityItem),
      );
      const constructorEntitiesToAdd = constructorEntities.filter(constructorEntityItem => {
        const constructorEntityIdentifier = this.getConstructorEntityIdentifier(constructorEntityItem);
        if (constructorEntityCollectionIdentifiers.includes(constructorEntityIdentifier)) {
          return false;
        }
        constructorEntityCollectionIdentifiers.push(constructorEntityIdentifier);
        return true;
      });
      return [...constructorEntitiesToAdd, ...constructorEntityCollection];
    }
    return constructorEntityCollection;
  }
}
