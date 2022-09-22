import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'poligono',
        data: { pageTitle: 'Poligonos' },
        loadChildren: () => import('./poligono/poligono.module').then(m => m.PoligonoModule),
      },
      {
        path: 'forma',
        data: { pageTitle: 'Formas' },
        loadChildren: () => import('./forma/forma.module').then(m => m.FormaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
