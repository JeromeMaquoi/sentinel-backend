import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../joular-node-entity.test-samples';

import { JoularNodeEntityFormService } from './joular-node-entity-form.service';

describe('JoularNodeEntity Form Service', () => {
  let service: JoularNodeEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JoularNodeEntityFormService);
  });

  describe('Service methods', () => {
    describe('createJoularNodeEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createJoularNodeEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lineNumber: expect.any(Object),
            value: expect.any(Object),
          }),
        );
      });

      it('passing IJoularNodeEntity should create a new form with FormGroup', () => {
        const formGroup = service.createJoularNodeEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lineNumber: expect.any(Object),
            value: expect.any(Object),
          }),
        );
      });
    });

    describe('getJoularNodeEntity', () => {
      it('should return NewJoularNodeEntity for default JoularNodeEntity initial value', () => {
        const formGroup = service.createJoularNodeEntityFormGroup(sampleWithNewData);

        const joularNodeEntity = service.getJoularNodeEntity(formGroup) as any;

        expect(joularNodeEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewJoularNodeEntity for empty JoularNodeEntity initial value', () => {
        const formGroup = service.createJoularNodeEntityFormGroup();

        const joularNodeEntity = service.getJoularNodeEntity(formGroup) as any;

        expect(joularNodeEntity).toMatchObject({});
      });

      it('should return IJoularNodeEntity', () => {
        const formGroup = service.createJoularNodeEntityFormGroup(sampleWithRequiredData);

        const joularNodeEntity = service.getJoularNodeEntity(formGroup) as any;

        expect(joularNodeEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IJoularNodeEntity should not enable id FormControl', () => {
        const formGroup = service.createJoularNodeEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewJoularNodeEntity should disable id FormControl', () => {
        const formGroup = service.createJoularNodeEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
