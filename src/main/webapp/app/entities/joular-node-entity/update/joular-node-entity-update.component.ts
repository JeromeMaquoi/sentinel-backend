import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IJoularNodeEntity } from '../joular-node-entity.model';
import { JoularNodeEntityService } from '../service/joular-node-entity.service';
import { JoularNodeEntityFormService, JoularNodeEntityFormGroup } from './joular-node-entity-form.service';

@Component({
  standalone: true,
  selector: 'jhi-joular-node-entity-update',
  templateUrl: './joular-node-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class JoularNodeEntityUpdateComponent implements OnInit {
  isSaving = false;
  joularNodeEntity: IJoularNodeEntity | null = null;

  editForm: JoularNodeEntityFormGroup = this.joularNodeEntityFormService.createJoularNodeEntityFormGroup();

  constructor(
    protected joularNodeEntityService: JoularNodeEntityService,
    protected joularNodeEntityFormService: JoularNodeEntityFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ joularNodeEntity }) => {
      this.joularNodeEntity = joularNodeEntity;
      if (joularNodeEntity) {
        this.updateForm(joularNodeEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const joularNodeEntity = this.joularNodeEntityFormService.getJoularNodeEntity(this.editForm);
    if (joularNodeEntity.id !== null) {
      this.subscribeToSaveResponse(this.joularNodeEntityService.update(joularNodeEntity));
    } else {
      this.subscribeToSaveResponse(this.joularNodeEntityService.create(joularNodeEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJoularNodeEntity>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(joularNodeEntity: IJoularNodeEntity): void {
    this.joularNodeEntity = joularNodeEntity;
    this.joularNodeEntityFormService.resetForm(this.editForm, joularNodeEntity);
  }
}
