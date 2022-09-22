import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PoligonoComponent } from './list/poligono.component';
import { PoligonoDetailComponent } from './detail/poligono-detail.component';
import { PoligonoUpdateComponent } from './update/poligono-update.component';
import { PoligonoDeleteDialogComponent } from './delete/poligono-delete-dialog.component';
import { PoligonoRoutingModule } from './route/poligono-routing.module';

@NgModule({
  imports: [SharedModule, PoligonoRoutingModule],
  declarations: [PoligonoComponent, PoligonoDetailComponent, PoligonoUpdateComponent, PoligonoDeleteDialogComponent],
})
export class PoligonoModule {}
