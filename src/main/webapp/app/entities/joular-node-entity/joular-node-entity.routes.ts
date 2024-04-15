import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { JoularNodeEntityComponent } from './list/joular-node-entity.component';
import { JoularNodeEntityDetailComponent } from './detail/joular-node-entity-detail.component';
import { JoularNodeEntityUpdateComponent } from './update/joular-node-entity-update.component';
import JoularNodeEntityResolve from './route/joular-node-entity-routing-resolve.service';

const joularNodeEntityRoute: Routes = [
  {
    path: '',
    component: JoularNodeEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JoularNodeEntityDetailComponent,
    resolve: {
      joularNodeEntity: JoularNodeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JoularNodeEntityUpdateComponent,
    resolve: {
      joularNodeEntity: JoularNodeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JoularNodeEntityUpdateComponent,
    resolve: {
      joularNodeEntity: JoularNodeEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default joularNodeEntityRoute;
