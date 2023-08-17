import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CkEntityComponent } from './list/ck-entity.component';
import { CkEntityDetailComponent } from './detail/ck-entity-detail.component';
import { CkEntityUpdateComponent } from './update/ck-entity-update.component';
import CkEntityResolve from './route/ck-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ckEntityRoute: Routes = [
  {
    path: '',
    component: CkEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CkEntityDetailComponent,
    resolve: {
      ckEntity: CkEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CkEntityUpdateComponent,
    resolve: {
      ckEntity: CkEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CkEntityUpdateComponent,
    resolve: {
      ckEntity: CkEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ckEntityRoute;
