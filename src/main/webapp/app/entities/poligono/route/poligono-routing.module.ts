import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PoligonoComponent } from '../list/poligono.component';
import { PoligonoDetailComponent } from '../detail/poligono-detail.component';
import { PoligonoUpdateComponent } from '../update/poligono-update.component';
import { PoligonoRoutingResolveService } from './poligono-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const poligonoRoute: Routes = [
  {
    path: '',
    component: PoligonoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PoligonoDetailComponent,
    resolve: {
      poligono: PoligonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PoligonoUpdateComponent,
    resolve: {
      poligono: PoligonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PoligonoUpdateComponent,
    resolve: {
      poligono: PoligonoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(poligonoRoute)],
  exports: [RouterModule],
})
export class PoligonoRoutingModule {}
