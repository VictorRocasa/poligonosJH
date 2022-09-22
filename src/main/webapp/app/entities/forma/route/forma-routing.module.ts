import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormaComponent } from '../list/forma.component';
import { FormaDetailComponent } from '../detail/forma-detail.component';
import { FormaUpdateComponent } from '../update/forma-update.component';
import { FormaRoutingResolveService } from './forma-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const formaRoute: Routes = [
  {
    path: '',
    component: FormaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormaDetailComponent,
    resolve: {
      forma: FormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormaUpdateComponent,
    resolve: {
      forma: FormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormaUpdateComponent,
    resolve: {
      forma: FormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formaRoute)],
  exports: [RouterModule],
})
export class FormaRoutingModule {}
