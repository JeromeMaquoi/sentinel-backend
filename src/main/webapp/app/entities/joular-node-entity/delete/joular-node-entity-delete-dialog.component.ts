import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IJoularNodeEntity } from '../joular-node-entity.model';
import { JoularNodeEntityService } from '../service/joular-node-entity.service';

@Component({
  standalone: true,
  templateUrl: './joular-node-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class JoularNodeEntityDeleteDialogComponent {
  joularNodeEntity?: IJoularNodeEntity;

  constructor(
    protected joularNodeEntityService: JoularNodeEntityService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.joularNodeEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
