import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IJoularNodeEntity } from '../joular-node-entity.model';

@Component({
  standalone: true,
  selector: 'jhi-joular-node-entity-detail',
  templateUrl: './joular-node-entity-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class JoularNodeEntityDetailComponent {
  @Input() joularNodeEntity: IJoularNodeEntity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
