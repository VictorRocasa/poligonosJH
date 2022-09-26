import { Route } from '@angular/router';

import { EstoqueComponent } from './estoque.component';

export const HOME_ROUTE: Route = {
  path: 'estoque',
  component: EstoqueComponent,
  data: {
    pageTitle: 'Estoque',
  },
};
