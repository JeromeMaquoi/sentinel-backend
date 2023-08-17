import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CkEntityFormService, CkEntityFormGroup } from './ck-entity-form.service';
import { ICkEntity } from '../ck-entity.model';
import { CkEntityService } from '../service/ck-entity.service';

@Component({
  standalone: true,
  selector: 'jhi-ck-entity-update',
  templateUrl: './ck-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CkEntityUpdateComponent implements OnInit {
  isSaving = false;
  ckEntity: ICkEntity | null = null;

  editForm: CkEntityFormGroup = this.ckEntityFormService.createCkEntityFormGroup();

  constructor(
    protected ckEntityService: CkEntityService,
    protected ckEntityFormService: CkEntityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ckEntity }) => {
      this.ckEntity = ckEntity;
      if (ckEntity) {
        this.updateForm(ckEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ckEntity = this.ckEntityFormService.getCkEntity(this.editForm);
    if (ckEntity.id !== null) {
      this.subscribeToSaveResponse(this.ckEntityService.update(ckEntity));
    } else {
      this.subscribeToSaveResponse(this.ckEntityService.create(ckEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICkEntity>>): void {
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

  protected updateForm(ckEntity: ICkEntity): void {
    this.ckEntity = ckEntity;
    this.ckEntityFormService.resetForm(this.editForm, ckEntity);
  }
}
