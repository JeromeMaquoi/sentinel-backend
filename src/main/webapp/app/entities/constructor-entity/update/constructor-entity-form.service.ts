import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IConstructorEntity, NewConstructorEntity } from '../constructor-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConstructorEntity for edit and NewConstructorEntityFormGroupInput for create.
 */
type ConstructorEntityFormGroupInput = IConstructorEntity | PartialWithRequiredKeyOf<NewConstructorEntity>;

type ConstructorEntityFormDefaults = Pick<NewConstructorEntity, 'id'>;

type ConstructorEntityFormGroupContent = {
  id: FormControl<IConstructorEntity['id'] | NewConstructorEntity['id']>;
  name: FormControl<IConstructorEntity['name']>;
  signature: FormControl<IConstructorEntity['signature']>;
  pkg: FormControl<IConstructorEntity['pkg']>;
  file: FormControl<IConstructorEntity['file']>;
};

export type ConstructorEntityFormGroup = FormGroup<ConstructorEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConstructorEntityFormService {
  createConstructorEntityFormGroup(constructorEntity: ConstructorEntityFormGroupInput = { id: null }): ConstructorEntityFormGroup {
    const constructorEntityRawValue = {
      ...this.getFormDefaults(),
      ...constructorEntity,
    };
    return new FormGroup<ConstructorEntityFormGroupContent>({
      id: new FormControl(
        { value: constructorEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(constructorEntityRawValue.name),
      signature: new FormControl(constructorEntityRawValue.signature),
      pkg: new FormControl(constructorEntityRawValue.pkg),
      file: new FormControl(constructorEntityRawValue.file),
    });
  }

  getConstructorEntity(form: ConstructorEntityFormGroup): IConstructorEntity | NewConstructorEntity {
    return form.getRawValue() as IConstructorEntity | NewConstructorEntity;
  }

  resetForm(form: ConstructorEntityFormGroup, constructorEntity: ConstructorEntityFormGroupInput): void {
    const constructorEntityRawValue = { ...this.getFormDefaults(), ...constructorEntity };
    form.reset(
      {
        ...constructorEntityRawValue,
        id: { value: constructorEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConstructorEntityFormDefaults {
    return {
      id: null,
    };
  }
}
