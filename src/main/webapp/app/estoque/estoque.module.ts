import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './estoque.route';
import { EstoqueComponent } from './estoque.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE])],
  exports: [EstoqueComponent],
  declarations: [EstoqueComponent],
})
export class EstoqueModule {}
