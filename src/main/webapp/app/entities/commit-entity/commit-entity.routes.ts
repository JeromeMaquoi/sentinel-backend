import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommitEntityComponent } from './list/commit-entity.component';
import { CommitEntityDetailComponent } from './detail/commit-entity-detail.component';
import { CommitEntityUpdateComponent } from './update/commit-entity-update.component';
import CommitEntityResolve from './route/commit-entity-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const commitEntityRoute: Routes = [
  {
    path: '',
    component: CommitEntityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommitEntityDetailComponent,
    resolve: {
      commitEntity: CommitEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommitEntityUpdateComponent,
    resolve: {
      commitEntity: CommitEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommitEntityUpdateComponent,
    resolve: {
      commitEntity: CommitEntityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commitEntityRoute;
