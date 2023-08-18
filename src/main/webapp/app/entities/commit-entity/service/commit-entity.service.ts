import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommitEntity, NewCommitEntity } from '../commit-entity.model';

export type PartialUpdateCommitEntity = Partial<ICommitEntity> & Pick<ICommitEntity, 'id'>;

export type EntityResponseType = HttpResponse<ICommitEntity>;
export type EntityArrayResponseType = HttpResponse<ICommitEntity[]>;

@Injectable({ providedIn: 'root' })
export class CommitEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commit-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commitEntity: NewCommitEntity): Observable<EntityResponseType> {
    return this.http.post<ICommitEntity>(this.resourceUrl, commitEntity, { observe: 'response' });
  }

  update(commitEntity: ICommitEntity): Observable<EntityResponseType> {
    return this.http.put<ICommitEntity>(`${this.resourceUrl}/${this.getCommitEntityIdentifier(commitEntity)}`, commitEntity, {
      observe: 'response',
    });
  }

  partialUpdate(commitEntity: PartialUpdateCommitEntity): Observable<EntityResponseType> {
    return this.http.patch<ICommitEntity>(`${this.resourceUrl}/${this.getCommitEntityIdentifier(commitEntity)}`, commitEntity, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICommitEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommitEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommitEntityIdentifier(commitEntity: Pick<ICommitEntity, 'id'>): string {
    return commitEntity.id;
  }

  compareCommitEntity(o1: Pick<ICommitEntity, 'id'> | null, o2: Pick<ICommitEntity, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommitEntityIdentifier(o1) === this.getCommitEntityIdentifier(o2) : o1 === o2;
  }

  addCommitEntityToCollectionIfMissing<Type extends Pick<ICommitEntity, 'id'>>(
    commitEntityCollection: Type[],
    ...commitEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commitEntities: Type[] = commitEntitiesToCheck.filter(isPresent);
    if (commitEntities.length > 0) {
      const commitEntityCollectionIdentifiers = commitEntityCollection.map(
        commitEntityItem => this.getCommitEntityIdentifier(commitEntityItem)!
      );
      const commitEntitiesToAdd = commitEntities.filter(commitEntityItem => {
        const commitEntityIdentifier = this.getCommitEntityIdentifier(commitEntityItem);
        if (commitEntityCollectionIdentifiers.includes(commitEntityIdentifier)) {
          return false;
        }
        commitEntityCollectionIdentifiers.push(commitEntityIdentifier);
        return true;
      });
      return [...commitEntitiesToAdd, ...commitEntityCollection];
    }
    return commitEntityCollection;
  }
}
