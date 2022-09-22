import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoligono } from '../poligono.model';
import { PoligonoService } from '../service/poligono.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './poligono-delete-dialog.component.html',
})
export class PoligonoDeleteDialogComponent {
  poligono?: IPoligono;

  constructor(protected poligonoService: PoligonoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.poligonoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
