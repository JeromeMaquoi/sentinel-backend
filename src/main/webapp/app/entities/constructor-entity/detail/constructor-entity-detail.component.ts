import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IConstructorEntity } from '../constructor-entity.model';

@Component({
  standalone: true,
  selector: 'jhi-constructor-entity-detail',
  templateUrl: './constructor-entity-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConstructorEntityDetailComponent {
  constructorEntity = input<IConstructorEntity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
