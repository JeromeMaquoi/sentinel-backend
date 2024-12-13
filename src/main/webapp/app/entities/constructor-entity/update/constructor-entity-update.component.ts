import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConstructorEntity } from '../constructor-entity.model';
import { ConstructorEntityService } from '../service/constructor-entity.service';
import { ConstructorEntityFormGroup, ConstructorEntityFormService } from './constructor-entity-form.service';

@Component({
  standalone: true,
  selector: 'jhi-constructor-entity-update',
  templateUrl: './constructor-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConstructorEntityUpdateComponent implements OnInit {
  isSaving = false;
  constructorEntity: IConstructorEntity | null = null;

  protected constructorEntityService = inject(ConstructorEntityService);
  protected constructorEntityFormService = inject(ConstructorEntityFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConstructorEntityFormGroup = this.constructorEntityFormService.createConstructorEntityFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ constructorEntity }) => {
      this.constructorEntity = constructorEntity;
      if (constructorEntity) {
        this.updateForm(constructorEntity);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const constructorEntity = this.constructorEntityFormService.getConstructorEntity(this.editForm);
    if (constructorEntity.id !== null) {
      this.subscribeToSaveResponse(this.constructorEntityService.update(constructorEntity));
    } else {
      this.subscribeToSaveResponse(this.constructorEntityService.create(constructorEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConstructorEntity>>): void {
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

  protected updateForm(constructorEntity: IConstructorEntity): void {
    this.constructorEntity = constructorEntity;
    this.constructorEntityFormService.resetForm(this.editForm, constructorEntity);
  }
}
