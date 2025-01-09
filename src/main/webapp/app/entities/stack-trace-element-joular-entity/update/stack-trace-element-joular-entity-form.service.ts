import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IStackTraceElementJoularEntity, NewStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStackTraceElementJoularEntity for edit and NewStackTraceElementJoularEntityFormGroupInput for create.
 */
type StackTraceElementJoularEntityFormGroupInput =
  | IStackTraceElementJoularEntity
  | PartialWithRequiredKeyOf<NewStackTraceElementJoularEntity>;

type StackTraceElementJoularEntityFormDefaults = Pick<NewStackTraceElementJoularEntity, 'id'>;

type StackTraceElementJoularEntityFormGroupContent = {
  id: FormControl<IStackTraceElementJoularEntity['id'] | NewStackTraceElementJoularEntity['id']>;
  lineNumber: FormControl<IStackTraceElementJoularEntity['lineNumber']>;
  constructorElement: FormControl<IStackTraceElementJoularEntity['constructorElement']>;
  parent: FormControl<IStackTraceElementJoularEntity['parent']>;
  ancestors: FormControl<IStackTraceElementJoularEntity['ancestors']>;
  consumptionValues: FormControl<IStackTraceElementJoularEntity['consumptionValues']>;
  commit: FormControl<IStackTraceElementJoularEntity['commit']>;
};

export type StackTraceElementJoularEntityFormGroup = FormGroup<StackTraceElementJoularEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StackTraceElementJoularEntityFormService {
  createStackTraceElementJoularEntityFormGroup(
    stackTraceElementJoularEntity: StackTraceElementJoularEntityFormGroupInput = { id: null },
  ): StackTraceElementJoularEntityFormGroup {
    const stackTraceElementJoularEntityRawValue = {
      ...this.getFormDefaults(),
      ...stackTraceElementJoularEntity,
    };
    return new FormGroup<StackTraceElementJoularEntityFormGroupContent>({
      id: new FormControl(
        { value: stackTraceElementJoularEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      lineNumber: new FormControl(stackTraceElementJoularEntityRawValue.lineNumber),
      constructorElement: new FormControl(stackTraceElementJoularEntityRawValue.constructorElement),
      parent: new FormControl(stackTraceElementJoularEntityRawValue.parent),
      ancestors: new FormControl(stackTraceElementJoularEntityRawValue.ancestors),
      consumptionValues: new FormControl(stackTraceElementJoularEntityRawValue.consumptionValues),
      commit: new FormControl(stackTraceElementJoularEntityRawValue.commit),
    });
  }

  getStackTraceElementJoularEntity(
    form: StackTraceElementJoularEntityFormGroup,
  ): IStackTraceElementJoularEntity | NewStackTraceElementJoularEntity {
    return form.getRawValue() as IStackTraceElementJoularEntity | NewStackTraceElementJoularEntity;
  }

  resetForm(
    form: StackTraceElementJoularEntityFormGroup,
    stackTraceElementJoularEntity: StackTraceElementJoularEntityFormGroupInput,
  ): void {
    const stackTraceElementJoularEntityRawValue = { ...this.getFormDefaults(), ...stackTraceElementJoularEntity };
    form.reset(
      {
        ...stackTraceElementJoularEntityRawValue,
        id: { value: stackTraceElementJoularEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StackTraceElementJoularEntityFormDefaults {
    return {
      id: null,
    };
  }
}
