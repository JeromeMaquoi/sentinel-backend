import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConstructorEntity } from '../constructor-entity.model';
import { ConstructorEntityService } from '../service/constructor-entity.service';

const constructorEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IConstructorEntity> => {
  const id = route.params.id;
  if (id) {
    return inject(ConstructorEntityService)
      .find(id)
      .pipe(
        mergeMap((constructorEntity: HttpResponse<IConstructorEntity>) => {
          if (constructorEntity.body) {
            return of(constructorEntity.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default constructorEntityResolve;
