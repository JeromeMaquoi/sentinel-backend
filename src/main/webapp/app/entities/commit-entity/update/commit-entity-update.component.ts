import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CommitEntityFormService, CommitEntityFormGroup } from './commit-entity-form.service';
import { ICommitEntity } from '../commit-entity.model';
import { CommitEntityService } from '../service/commit-entity.service';

@Component({
  standalone: true,
  selector: 'jhi-commit-entity-update',
  templateUrl: './commit-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommitEntityUpdateComponent implements OnInit {
  isSaving = false;
  commitEntity: ICommitEntity | null = null;

  editForm: CommitEntityFormGroup = this.commitEntityFormService.createCommitEntityFormGroup();

  constructor(
    protected commitEntityService: CommitEntityService,
    protected commitEntityFormService: CommitEntityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commitEntity }) => {
      this.commitEntity = commitEntity;
      if (commitEntity) {
        this.updateForm(commitEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commitEntity = this.commitEntityFormService.getCommitEntity(this.editForm);
    if (commitEntity.id !== null) {
      this.subscribeToSaveResponse(this.commitEntityService.update(commitEntity));
    } else {
      this.subscribeToSaveResponse(this.commitEntityService.create(commitEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommitEntity>>): void {
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

  protected updateForm(commitEntity: ICommitEntity): void {
    this.commitEntity = commitEntity;
    this.commitEntityFormService.resetForm(this.editForm, commitEntity);
  }
}
