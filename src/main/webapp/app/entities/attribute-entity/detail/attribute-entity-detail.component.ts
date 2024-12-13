import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAttributeEntity } from '../attribute-entity.model';

@Component({
  standalone: true,
  selector: 'jhi-attribute-entity-detail',
  templateUrl: './attribute-entity-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AttributeEntityDetailComponent {
  attributeEntity = input<IAttributeEntity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
