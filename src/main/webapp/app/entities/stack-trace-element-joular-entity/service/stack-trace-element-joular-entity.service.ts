import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStackTraceElementJoularEntity, NewStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';

export type PartialUpdateStackTraceElementJoularEntity = Partial<IStackTraceElementJoularEntity> &
  Pick<IStackTraceElementJoularEntity, 'id'>;

export type EntityResponseType = HttpResponse<IStackTraceElementJoularEntity>;
export type EntityArrayResponseType = HttpResponse<IStackTraceElementJoularEntity[]>;

@Injectable({ providedIn: 'root' })
export class StackTraceElementJoularEntityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stack-trace-element-joular-entities');

  create(stackTraceElementJoularEntity: NewStackTraceElementJoularEntity): Observable<EntityResponseType> {
    return this.http.post<IStackTraceElementJoularEntity>(this.resourceUrl, stackTraceElementJoularEntity, { observe: 'response' });
  }

  update(stackTraceElementJoularEntity: IStackTraceElementJoularEntity): Observable<EntityResponseType> {
    return this.http.put<IStackTraceElementJoularEntity>(
      `${this.resourceUrl}/${this.getStackTraceElementJoularEntityIdentifier(stackTraceElementJoularEntity)}`,
      stackTraceElementJoularEntity,
      { observe: 'response' },
    );
  }

  partialUpdate(stackTraceElementJoularEntity: PartialUpdateStackTraceElementJoularEntity): Observable<EntityResponseType> {
    return this.http.patch<IStackTraceElementJoularEntity>(
      `${this.resourceUrl}/${this.getStackTraceElementJoularEntityIdentifier(stackTraceElementJoularEntity)}`,
      stackTraceElementJoularEntity,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IStackTraceElementJoularEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStackTraceElementJoularEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStackTraceElementJoularEntityIdentifier(stackTraceElementJoularEntity: Pick<IStackTraceElementJoularEntity, 'id'>): string {
    return stackTraceElementJoularEntity.id;
  }

  compareStackTraceElementJoularEntity(
    o1: Pick<IStackTraceElementJoularEntity, 'id'> | null,
    o2: Pick<IStackTraceElementJoularEntity, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getStackTraceElementJoularEntityIdentifier(o1) === this.getStackTraceElementJoularEntityIdentifier(o2)
      : o1 === o2;
  }

  addStackTraceElementJoularEntityToCollectionIfMissing<Type extends Pick<IStackTraceElementJoularEntity, 'id'>>(
    stackTraceElementJoularEntityCollection: Type[],
    ...stackTraceElementJoularEntitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stackTraceElementJoularEntities: Type[] = stackTraceElementJoularEntitiesToCheck.filter(isPresent);
    if (stackTraceElementJoularEntities.length > 0) {
      const stackTraceElementJoularEntityCollectionIdentifiers = stackTraceElementJoularEntityCollection.map(
        stackTraceElementJoularEntityItem => this.getStackTraceElementJoularEntityIdentifier(stackTraceElementJoularEntityItem),
      );
      const stackTraceElementJoularEntitiesToAdd = stackTraceElementJoularEntities.filter(stackTraceElementJoularEntityItem => {
        const stackTraceElementJoularEntityIdentifier = this.getStackTraceElementJoularEntityIdentifier(stackTraceElementJoularEntityItem);
        if (stackTraceElementJoularEntityCollectionIdentifiers.includes(stackTraceElementJoularEntityIdentifier)) {
          return false;
        }
        stackTraceElementJoularEntityCollectionIdentifiers.push(stackTraceElementJoularEntityIdentifier);
        return true;
      });
      return [...stackTraceElementJoularEntitiesToAdd, ...stackTraceElementJoularEntityCollection];
    }
    return stackTraceElementJoularEntityCollection;
  }
}
