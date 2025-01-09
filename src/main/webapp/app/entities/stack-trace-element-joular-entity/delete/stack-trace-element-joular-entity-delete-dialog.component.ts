import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import { StackTraceElementJoularEntityService } from '../service/stack-trace-element-joular-entity.service';

@Component({
  standalone: true,
  templateUrl: './stack-trace-element-joular-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StackTraceElementJoularEntityDeleteDialogComponent {
  stackTraceElementJoularEntity?: IStackTraceElementJoularEntity;

  protected stackTraceElementJoularEntityService = inject(StackTraceElementJoularEntityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.stackTraceElementJoularEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
