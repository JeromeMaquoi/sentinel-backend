import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../commit-entity.test-samples';

import { CommitEntityFormService } from './commit-entity-form.service';

describe('CommitEntity Form Service', () => {
  let service: CommitEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommitEntityFormService);
  });

  describe('Service methods', () => {
    describe('createCommitEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommitEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sha: expect.any(Object),
          })
        );
      });

      it('passing ICommitEntity should create a new form with FormGroup', () => {
        const formGroup = service.createCommitEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sha: expect.any(Object),
          })
        );
      });
    });

    describe('getCommitEntity', () => {
      it('should return NewCommitEntity for default CommitEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCommitEntityFormGroup(sampleWithNewData);

        const commitEntity = service.getCommitEntity(formGroup) as any;

        expect(commitEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommitEntity for empty CommitEntity initial value', () => {
        const formGroup = service.createCommitEntityFormGroup();

        const commitEntity = service.getCommitEntity(formGroup) as any;

        expect(commitEntity).toMatchObject({});
      });

      it('should return ICommitEntity', () => {
        const formGroup = service.createCommitEntityFormGroup(sampleWithRequiredData);

        const commitEntity = service.getCommitEntity(formGroup) as any;

        expect(commitEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommitEntity should not enable id FormControl', () => {
        const formGroup = service.createCommitEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommitEntity should disable id FormControl', () => {
        const formGroup = service.createCommitEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
