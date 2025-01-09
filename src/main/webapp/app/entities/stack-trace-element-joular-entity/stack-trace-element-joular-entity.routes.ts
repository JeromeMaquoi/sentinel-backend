import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import StackTraceElementJoularEntityResolve from './route/stack-trace-element-joular-entity-routing-resolve.service';

const stackTraceElementJoularEntityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/stack-trace-element-joular-entity.component').then(m => m.StackTraceElementJoularEntityComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/stack-trace-element-joular-entity-detail.component').then(m => m.StackTraceElementJoularEntityDetailComponent),
    resolve: {
      stackTraceElementJoularEntity: StackTraceElementJoularEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/stack-trace-element-joular-entity-update.component').then(m => m.StackTraceElementJoularEntityUpdateComponent),
    resolve: {
      stackTraceElementJoularEntity: StackTraceElementJoularEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/stack-trace-element-joular-entity-update.component').then(m => m.StackTraceElementJoularEntityUpdateComponent),
    resolve: {
      stackTraceElementJoularEntity: StackTraceElementJoularEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stackTraceElementJoularEntityRoute;
