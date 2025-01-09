import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../stack-trace-element-joular-entity.test-samples';

import { StackTraceElementJoularEntityFormService } from './stack-trace-element-joular-entity-form.service';

describe('StackTraceElementJoularEntity Form Service', () => {
  let service: StackTraceElementJoularEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StackTraceElementJoularEntityFormService);
  });

  describe('Service methods', () => {
    describe('createStackTraceElementJoularEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lineNumber: expect.any(Object),
            constructorElement: expect.any(Object),
            parent: expect.any(Object),
            ancestors: expect.any(Object),
            consumptionValues: expect.any(Object),
            commit: expect.any(Object),
          }),
        );
      });

      it('passing IStackTraceElementJoularEntity should create a new form with FormGroup', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lineNumber: expect.any(Object),
            constructorElement: expect.any(Object),
            parent: expect.any(Object),
            ancestors: expect.any(Object),
            consumptionValues: expect.any(Object),
            commit: expect.any(Object),
          }),
        );
      });
    });

    describe('getStackTraceElementJoularEntity', () => {
      it('should return NewStackTraceElementJoularEntity for default StackTraceElementJoularEntity initial value', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup(sampleWithNewData);

        const stackTraceElementJoularEntity = service.getStackTraceElementJoularEntity(formGroup) as any;

        expect(stackTraceElementJoularEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewStackTraceElementJoularEntity for empty StackTraceElementJoularEntity initial value', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup();

        const stackTraceElementJoularEntity = service.getStackTraceElementJoularEntity(formGroup) as any;

        expect(stackTraceElementJoularEntity).toMatchObject({});
      });

      it('should return IStackTraceElementJoularEntity', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup(sampleWithRequiredData);

        const stackTraceElementJoularEntity = service.getStackTraceElementJoularEntity(formGroup) as any;

        expect(stackTraceElementJoularEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStackTraceElementJoularEntity should not enable id FormControl', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStackTraceElementJoularEntity should disable id FormControl', () => {
        const formGroup = service.createStackTraceElementJoularEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
