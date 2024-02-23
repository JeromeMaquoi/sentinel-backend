import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJoularNodeEntity } from '../joular-node-entity.model';
import { JoularNodeEntityService } from '../service/joular-node-entity.service';

export const joularNodeEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IJoularNodeEntity> => {
  const id = route.params['id'];
  if (id) {
    return inject(JoularNodeEntityService)
      .find(id)
      .pipe(
        mergeMap((joularNodeEntity: HttpResponse<IJoularNodeEntity>) => {
          if (joularNodeEntity.body) {
            return of(joularNodeEntity.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default joularNodeEntityResolve;
