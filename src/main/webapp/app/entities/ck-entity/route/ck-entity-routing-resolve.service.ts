import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICkEntity } from '../ck-entity.model';
import { CkEntityService } from '../service/ck-entity.service';

export const ckEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | ICkEntity> => {
  const id = route.params['id'];
  if (id) {
    return inject(CkEntityService)
      .find(id)
      .pipe(
        mergeMap((ckEntity: HttpResponse<ICkEntity>) => {
          if (ckEntity.body) {
            return of(ckEntity.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ckEntityResolve;
