import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ICommitEntity } from '../commit-entity.model';
import { CommitEntityService } from '../service/commit-entity.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './commit-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CommitEntityDeleteDialogComponent {
  commitEntity?: ICommitEntity;

  constructor(protected commitEntityService: CommitEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.commitEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
