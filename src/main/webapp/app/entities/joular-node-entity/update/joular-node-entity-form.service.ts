import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IJoularNodeEntity, NewJoularNodeEntity } from '../joular-node-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJoularNodeEntity for edit and NewJoularNodeEntityFormGroupInput for create.
 */
type JoularNodeEntityFormGroupInput = IJoularNodeEntity | PartialWithRequiredKeyOf<NewJoularNodeEntity>;

type JoularNodeEntityFormDefaults = Pick<NewJoularNodeEntity, 'id'>;

type JoularNodeEntityFormGroupContent = {
  id: FormControl<IJoularNodeEntity['id'] | NewJoularNodeEntity['id']>;
  lineNumber: FormControl<IJoularNodeEntity['lineNumber']>;
  value: FormControl<IJoularNodeEntity['value']>;
};

export type JoularNodeEntityFormGroup = FormGroup<JoularNodeEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JoularNodeEntityFormService {
  createJoularNodeEntityFormGroup(joularNodeEntity: JoularNodeEntityFormGroupInput = { id: null }): JoularNodeEntityFormGroup {
    const joularNodeEntityRawValue = {
      ...this.getFormDefaults(),
      ...joularNodeEntity,
    };
    return new FormGroup<JoularNodeEntityFormGroupContent>({
      id: new FormControl(
        { value: joularNodeEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      lineNumber: new FormControl(joularNodeEntityRawValue.lineNumber),
      value: new FormControl(joularNodeEntityRawValue.value),
    });
  }

  getJoularNodeEntity(form: JoularNodeEntityFormGroup): IJoularNodeEntity | NewJoularNodeEntity {
    return form.getRawValue() as IJoularNodeEntity | NewJoularNodeEntity;
  }

  resetForm(form: JoularNodeEntityFormGroup, joularNodeEntity: JoularNodeEntityFormGroupInput): void {
    const joularNodeEntityRawValue = { ...this.getFormDefaults(), ...joularNodeEntity };
    form.reset(
      {
        ...joularNodeEntityRawValue,
        id: { value: joularNodeEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JoularNodeEntityFormDefaults {
    return {
      id: null,
    };
  }
}
