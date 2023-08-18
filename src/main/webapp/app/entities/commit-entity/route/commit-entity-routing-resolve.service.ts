import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommitEntity } from '../commit-entity.model';
import { CommitEntityService } from '../service/commit-entity.service';

export const commitEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | ICommitEntity> => {
  const id = route.params['id'];
  if (id) {
    return inject(CommitEntityService)
      .find(id)
      .pipe(
        mergeMap((commitEntity: HttpResponse<ICommitEntity>) => {
          if (commitEntity.body) {
            return of(commitEntity.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default commitEntityResolve;
