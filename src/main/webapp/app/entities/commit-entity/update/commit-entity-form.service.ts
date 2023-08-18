import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICommitEntity, NewCommitEntity } from '../commit-entity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommitEntity for edit and NewCommitEntityFormGroupInput for create.
 */
type CommitEntityFormGroupInput = ICommitEntity | PartialWithRequiredKeyOf<NewCommitEntity>;

type CommitEntityFormDefaults = Pick<NewCommitEntity, 'id'>;

type CommitEntityFormGroupContent = {
  id: FormControl<ICommitEntity['id'] | NewCommitEntity['id']>;
  sha: FormControl<ICommitEntity['sha']>;
};

export type CommitEntityFormGroup = FormGroup<CommitEntityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommitEntityFormService {
  createCommitEntityFormGroup(commitEntity: CommitEntityFormGroupInput = { id: null }): CommitEntityFormGroup {
    const commitEntityRawValue = {
      ...this.getFormDefaults(),
      ...commitEntity,
    };
    return new FormGroup<CommitEntityFormGroupContent>({
      id: new FormControl(
        { value: commitEntityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      sha: new FormControl(commitEntityRawValue.sha, {
        validators: [Validators.required],
      }),
    });
  }

  getCommitEntity(form: CommitEntityFormGroup): ICommitEntity | NewCommitEntity {
    return form.getRawValue() as ICommitEntity | NewCommitEntity;
  }

  resetForm(form: CommitEntityFormGroup, commitEntity: CommitEntityFormGroupInput): void {
    const commitEntityRawValue = { ...this.getFormDefaults(), ...commitEntity };
    form.reset(
      {
        ...commitEntityRawValue,
        id: { value: commitEntityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommitEntityFormDefaults {
    return {
      id: null,
    };
  }
}
