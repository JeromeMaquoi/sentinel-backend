import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICkEntity, NewCkEntity } from '../ck-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICkEntity for edit and NewCkEntityFormGroupInput for create.
 */
type CkEntityFormGroupInput = ICkEntity | PartialWithRequiredKeyOf<NewCkEntity>;

type CkEntityFormDefaults = Pick<NewCkEntity, 'id'>;

type CkEntityFormGroupContent = {
  id: FormControl<ICkEntity['id'] | NewCkEntity['id']>;
  name: FormControl<ICkEntity['name']>;
  value: FormControl<ICkEntity['value']>;
  tool_version: FormControl<ICkEntity['tool_version']>;
};

export type CkEntityFormGroup = FormGroup<CkEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CkEntityFormService {
  createCkEntityFormGroup(ckEntity: CkEntityFormGroupInput = { id: null }): CkEntityFormGroup {
    const ckEntityRawValue = {
      ...this.getFormDefaults(),
      ...ckEntity,
    };
    return new FormGroup<CkEntityFormGroupContent>({
      id: new FormControl(
        { value: ckEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(ckEntityRawValue.name, {
        validators: [Validators.required],
      }),
      value: new FormControl(ckEntityRawValue.value, {
        validators: [Validators.required],
      }),
      tool_version: new FormControl(ckEntityRawValue.tool_version, {
        validators: [Validators.required],
      }),
    });
  }

  getCkEntity(form: CkEntityFormGroup): ICkEntity | NewCkEntity {
    return form.getRawValue() as ICkEntity | NewCkEntity;
  }

  resetForm(form: CkEntityFormGroup, ckEntity: CkEntityFormGroupInput): void {
    const ckEntityRawValue = { ...this.getFormDefaults(), ...ckEntity };
    form.reset(
      {
        ...ckEntityRawValue,
        id: { value: ckEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CkEntityFormDefaults {
    return {
      id: null,
    };
  }
}
