import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ICkEntity } from '../ck-entity.model';
import { CkEntityService } from '../service/ck-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './ck-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CkEntityDeleteDialogComponent {
  ckEntity?: ICkEntity;

  constructor(protected ckEntityService: CkEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.ckEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
