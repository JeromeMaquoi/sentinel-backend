import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ConstructorEntityResolve from './route/constructor-entity-routing-resolve.service';

const constructorEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/constructor-entity.component').then(m => m.ConstructorEntityComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/constructor-entity-detail.component').then(m => m.ConstructorEntityDetailComponent),
    resolve: {
      constructorEntity: ConstructorEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/constructor-entity-update.component').then(m => m.ConstructorEntityUpdateComponent),
    resolve: {
      constructorEntity: ConstructorEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/constructor-entity-update.component').then(m => m.ConstructorEntityUpdateComponent),
    resolve: {
      constructorEntity: ConstructorEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default constructorEntityRoute;
