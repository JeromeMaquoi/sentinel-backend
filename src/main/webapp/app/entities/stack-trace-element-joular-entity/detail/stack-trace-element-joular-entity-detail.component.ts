import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';

@Component({
  standalone: true,
  selector: 'jhi-stack-trace-element-joular-entity-detail',
  templateUrl: './stack-trace-element-joular-entity-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StackTraceElementJoularEntityDetailComponent {
  stackTraceElementJoularEntity = input<IStackTraceElementJoularEntity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
