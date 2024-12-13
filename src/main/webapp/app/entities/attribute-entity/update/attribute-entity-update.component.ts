import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConstructorEntity } from 'app/entities/constructor-entity/constructor-entity.model';
import { ConstructorEntityService } from 'app/entities/constructor-entity/service/constructor-entity.service';
import { IAttributeEntity } from '../attribute-entity.model';
import { AttributeEntityService } from '../service/attribute-entity.service';
import { AttributeEntityFormGroup, AttributeEntityFormService } from './attribute-entity-form.service';

@Component({
  standalone: true,
  selector: 'jhi-attribute-entity-update',
  templateUrl: './attribute-entity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttributeEntityUpdateComponent implements OnInit {
  isSaving = false;
  attributeEntity: IAttributeEntity | null = null;

  constructorEntitiesSharedCollection: IConstructorEntity[] = [];

  protected attributeEntityService = inject(AttributeEntityService);
  protected attributeEntityFormService = inject(AttributeEntityFormService);
  protected constructorEntityService = inject(ConstructorEntityService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AttributeEntityFormGroup = this.attributeEntityFormService.createAttributeEntityFormGroup();

  compareConstructorEntity = (o1: IConstructorEntity | null, o2: IConstructorEntity | null): boolean =>
    this.constructorEntityService.compareConstructorEntity(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributeEntity }) => {
      this.attributeEntity = attributeEntity;
      if (attributeEntity) {
        this.updateForm(attributeEntity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attributeEntity = this.attributeEntityFormService.getAttributeEntity(this.editForm);
    if (attributeEntity.id !== null) {
      this.subscribeToSaveResponse(this.attributeEntityService.update(attributeEntity));
    } else {
      this.subscribeToSaveResponse(this.attributeEntityService.create(attributeEntity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttributeEntity>>): void {
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

  protected updateForm(attributeEntity: IAttributeEntity): void {
    this.attributeEntity = attributeEntity;
    this.attributeEntityFormService.resetForm(this.editForm, attributeEntity);

    this.constructorEntitiesSharedCollection = this.constructorEntityService.addConstructorEntityToCollectionIfMissing<IConstructorEntity>(
      this.constructorEntitiesSharedCollection,
      attributeEntity.constructorEntity,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.constructorEntityService
      .query()
      .pipe(map((res: HttpResponse<IConstructorEntity[]>) => res.body ?? []))
      .pipe(
        map((constructorEntities: IConstructorEntity[]) =>
          this.constructorEntityService.addConstructorEntityToCollectionIfMissing<IConstructorEntity>(
            constructorEntities,
            this.attributeEntity?.constructorEntity,
          ),
        ),
      )
      .subscribe((constructorEntities: IConstructorEntity[]) => (this.constructorEntitiesSharedCollection = constructorEntities));
  }
}
