import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ck-entity.test-samples';

import { CkEntityFormService } from './ck-entity-form.service';

describe('CkEntity Form Service', () => {
  let service: CkEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CkEntityFormService);
  });

  describe('Service methods', () => {
    describe('createCkEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCkEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            tool_version: expect.any(Object),
          })
        );
      });

      it('passing ICkEntity should create a new form with FormGroup', () => {
        const formGroup = service.createCkEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            tool_version: expect.any(Object),
          })
        );
      });
    });

    describe('getCkEntity', () => {
      it('should return NewCkEntity for default CkEntity initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCkEntityFormGroup(sampleWithNewData);

        const ckEntity = service.getCkEntity(formGroup) as any;

        expect(ckEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewCkEntity for empty CkEntity initial value', () => {
        const formGroup = service.createCkEntityFormGroup();

        const ckEntity = service.getCkEntity(formGroup) as any;

        expect(ckEntity).toMatchObject({});
      });

      it('should return ICkEntity', () => {
        const formGroup = service.createCkEntityFormGroup(sampleWithRequiredData);

        const ckEntity = service.getCkEntity(formGroup) as any;

        expect(ckEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICkEntity should not enable id FormControl', () => {
        const formGroup = service.createCkEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCkEntity should disable id FormControl', () => {
        const formGroup = service.createCkEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
