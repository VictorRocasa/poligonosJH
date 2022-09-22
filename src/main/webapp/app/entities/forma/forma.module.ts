import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormaComponent } from './list/forma.component';
import { FormaDetailComponent } from './detail/forma-detail.component';
import { FormaUpdateComponent } from './update/forma-update.component';
import { FormaDeleteDialogComponent } from './delete/forma-delete-dialog.component';
import { FormaRoutingModule } from './route/forma-routing.module';

@NgModule({
  imports: [SharedModule, FormaRoutingModule],
  declarations: [FormaComponent, FormaDetailComponent, FormaUpdateComponent, FormaDeleteDialogComponent],
})
export class FormaModule {}
