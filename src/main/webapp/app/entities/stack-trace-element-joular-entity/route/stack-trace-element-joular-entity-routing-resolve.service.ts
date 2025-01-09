import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import { StackTraceElementJoularEntityService } from '../service/stack-trace-element-joular-entity.service';

const stackTraceElementJoularEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IStackTraceElementJoularEntity> => {
  const id = route.params.id;
  if (id) {
    return inject(StackTraceElementJoularEntityService)
      .find(id)
      .pipe(
        mergeMap((stackTraceElementJoularEntity: HttpResponse<IStackTraceElementJoularEntity>) => {
          if (stackTraceElementJoularEntity.body) {
            return of(stackTraceElementJoularEntity.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default stackTraceElementJoularEntityResolve;
