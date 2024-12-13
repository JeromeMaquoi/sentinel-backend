import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAttributeEntity } from '../attribute-entity.model';
import { AttributeEntityService } from '../service/attribute-entity.service';

@Component({
  standalone: true,
  templateUrl: './attribute-entity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AttributeEntityDeleteDialogComponent {
  attributeEntity?: IAttributeEntity;

  protected attributeEntityService = inject(AttributeEntityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.attributeEntityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
