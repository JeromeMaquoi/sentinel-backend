import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJoularNodeEntity, NewJoularNodeEntity } from '../joular-node-entity.model';

export type PartialUpdateJoularNodeEntity = Partial<IJoularNodeEntity> & Pick<IJoularNodeEntity, 'id'>;

export type EntityResponseType = HttpResponse<IJoularNodeEntity>;
export type EntityArrayResponseType = HttpResponse<IJoularNodeEntity[]>;

@Injectable({ providedIn: 'root' })
export class JoularNodeEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/joular-node-entities');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(joularNodeEntity: NewJoularNodeEntity): Observable<EntityResponseType> {
    return this.http.post<IJoularNodeEntity>(this.resourceUrl, joularNodeEntity, { observe: 'response' });
  }

  update(joularNodeEntity: IJoularNodeEntity): Observable<EntityResponseType> {
    return this.http.put<IJoularNodeEntity>(
      `${this.resourceUrl}/${this.getJoularNodeEntityIdentifier(joularNodeEntity)}`,
      joularNodeEntity,
      { observe: 'response' },
    );
  }

  partialUpdate(joularNodeEntity: PartialUpdateJoularNodeEntity): Observable<EntityResponseType> {
    return this.http.patch<IJoularNodeEntity>(
      `${this.resourceUrl}/${this.getJoularNodeEntityIdentifier(joularNodeEntity)}`,
      joularNodeEntity,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IJoularNodeEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJoularNodeEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJoularNodeEntityIdentifier(joularNodeEntity: Pick<IJoularNodeEntity, 'id'>): string {
    return joularNodeEntity.id;
  }

  compareJoularNodeEntity(o1: Pick<IJoularNodeEntity, 'id'> | null, o2: Pick<IJoularNodeEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getJoularNodeEntityIdentifier(o1) === this.getJoularNodeEntityIdentifier(o2) : o1 === o2;
  }

  addJoularNodeEntityToCollectionIfMissing<Type extends Pick<IJoularNodeEntity, 'id'>>(
    joularNodeEntityCollection: Type[],
    ...joularNodeEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const joularNodeEntities: Type[] = joularNodeEntitiesToCheck.filter(isPresent);
    if (joularNodeEntities.length > 0) {
      const joularNodeEntityCollectionIdentifiers = joularNodeEntityCollection.map(
        joularNodeEntityItem => this.getJoularNodeEntityIdentifier(joularNodeEntityItem)!,
      );
      const joularNodeEntitiesToAdd = joularNodeEntities.filter(joularNodeEntityItem => {
        const joularNodeEntityIdentifier = this.getJoularNodeEntityIdentifier(joularNodeEntityItem);
        if (joularNodeEntityCollectionIdentifiers.includes(joularNodeEntityIdentifier)) {
          return false;
        }
        joularNodeEntityCollectionIdentifiers.push(joularNodeEntityIdentifier);
        return true;
      });
      return [...joularNodeEntitiesToAdd, ...joularNodeEntityCollection];
    }
    return joularNodeEntityCollection;
  }
}
