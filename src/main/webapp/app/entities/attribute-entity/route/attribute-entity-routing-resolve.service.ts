import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttributeEntity } from '../attribute-entity.model';
import { AttributeEntityService } from '../service/attribute-entity.service';

const attributeEntityResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttributeEntity> => {
  const id = route.params.id;
  if (id) {
    return inject(AttributeEntityService)
      .find(id)
      .pipe(
        mergeMap((attributeEntity: HttpResponse<IAttributeEntity>) => {
          if (attributeEntity.body) {
            return of(attributeEntity.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default attributeEntityResolve;
