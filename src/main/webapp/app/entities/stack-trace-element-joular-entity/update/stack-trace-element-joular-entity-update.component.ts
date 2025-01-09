import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import { StackTraceElementJoularEntityService } from '../service/stack-trace-element-joular-entity.service';
import {
  StackTraceElementJoularEntityFormGroup,
  StackTraceElementJoularEntityFormService,
} from './stack-trace-element-joular-entity-form.service';

@Component({
  standalone: true,
  selector: 'jhi-stack-trace-element-joular-entity-update',
  templateUrl: './stack-trace-element-joular-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StackTraceElementJoularEntityUpdateComponent implements OnInit {
  isSaving = false;
  stackTraceElementJoularEntity: IStackTraceElementJoularEntity | null = null;

  protected stackTraceElementJoularEntityService = inject(StackTraceElementJoularEntityService);
  protected stackTraceElementJoularEntityFormService = inject(StackTraceElementJoularEntityFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StackTraceElementJoularEntityFormGroup =
    this.stackTraceElementJoularEntityFormService.createStackTraceElementJoularEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stackTraceElementJoularEntity }) => {
      this.stackTraceElementJoularEntity = stackTraceElementJoularEntity;
      if (stackTraceElementJoularEntity) {
        this.updateForm(stackTraceElementJoularEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stackTraceElementJoularEntity = this.stackTraceElementJoularEntityFormService.getStackTraceElementJoularEntity(this.editForm);
    if (stackTraceElementJoularEntity.id !== null) {
      this.subscribeToSaveResponse(this.stackTraceElementJoularEntityService.update(stackTraceElementJoularEntity));
    } else {
      this.subscribeToSaveResponse(this.stackTraceElementJoularEntityService.create(stackTraceElementJoularEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStackTraceElementJoularEntity>>): void {
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

  protected updateForm(stackTraceElementJoularEntity: IStackTraceElementJoularEntity): void {
    this.stackTraceElementJoularEntity = stackTraceElementJoularEntity;
    this.stackTraceElementJoularEntityFormService.resetForm(this.editForm, stackTraceElementJoularEntity);
  }
}
