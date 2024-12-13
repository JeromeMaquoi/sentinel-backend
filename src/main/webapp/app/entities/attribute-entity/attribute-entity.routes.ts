import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import AttributeEntityResolve from './route/attribute-entity-routing-resolve.service';

const attributeEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/attribute-entity.component').then(m => m.AttributeEntityComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/attribute-entity-detail.component').then(m => m.AttributeEntityDetailComponent),
    resolve: {
      attributeEntity: AttributeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/attribute-entity-update.component').then(m => m.AttributeEntityUpdateComponent),
    resolve: {
      attributeEntity: AttributeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/attribute-entity-update.component').then(m => m.AttributeEntityUpdateComponent),
    resolve: {
      attributeEntity: AttributeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attributeEntityRoute;
