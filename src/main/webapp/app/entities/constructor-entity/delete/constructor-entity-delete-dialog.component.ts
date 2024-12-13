import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConstructorEntity } from '../constructor-entity.model';
import { ConstructorEntityService } from '../service/constructor-entity.service';

@Component({
  standalone: true,
  templateUrl: './constructor-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConstructorEntityDeleteDialogComponent {
  constructorEntity?: IConstructorEntity;

  protected constructorEntityService = inject(ConstructorEntityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.constructorEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
