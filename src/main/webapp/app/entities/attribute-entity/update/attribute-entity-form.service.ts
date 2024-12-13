import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAttributeEntity, NewAttributeEntity } from '../attribute-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttributeEntity for edit and NewAttributeEntityFormGroupInput for create.
 */
type AttributeEntityFormGroupInput = IAttributeEntity | PartialWithRequiredKeyOf<NewAttributeEntity>;

type AttributeEntityFormDefaults = Pick<NewAttributeEntity, 'id'>;

type AttributeEntityFormGroupContent = {
  id: FormControl<IAttributeEntity['id'] | NewAttributeEntity['id']>;
  name: FormControl<IAttributeEntity['name']>;
  type: FormControl<IAttributeEntity['type']>;
  constructorEntity: FormControl<IAttributeEntity['constructorEntity']>;
};

export type AttributeEntityFormGroup = FormGroup<AttributeEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttributeEntityFormService {
  createAttributeEntityFormGroup(attributeEntity: AttributeEntityFormGroupInput = { id: null }): AttributeEntityFormGroup {
    const attributeEntityRawValue = {
      ...this.getFormDefaults(),
      ...attributeEntity,
    };
    return new FormGroup<AttributeEntityFormGroupContent>({
      id: new FormControl(
        { value: attributeEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(attributeEntityRawValue.name),
      type: new FormControl(attributeEntityRawValue.type),
      constructorEntity: new FormControl(attributeEntityRawValue.constructorEntity),
    });
  }

  getAttributeEntity(form: AttributeEntityFormGroup): IAttributeEntity | NewAttributeEntity {
    return form.getRawValue() as IAttributeEntity | NewAttributeEntity;
  }

  resetForm(form: AttributeEntityFormGroup, attributeEntity: AttributeEntityFormGroupInput): void {
    const attributeEntityRawValue = { ...this.getFormDefaults(), ...attributeEntity };
    form.reset(
      {
        ...attributeEntityRawValue,
        id: { value: attributeEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AttributeEntityFormDefaults {
    return {
      id: null,
    };
  }
}
