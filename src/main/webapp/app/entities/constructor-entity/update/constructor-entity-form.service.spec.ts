import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../constructor-entity.test-samples';

import { ConstructorEntityFormService } from './constructor-entity-form.service';

describe('ConstructorEntity Form Service', () => {
  let service: ConstructorEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConstructorEntityFormService);
  });

  describe('Service methods', () => {
    describe('createConstructorEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConstructorEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            signature: expect.any(Object),
            pkg: expect.any(Object),
            file: expect.any(Object),
          }),
        );
      });

      it('passing IConstructorEntity should create a new form with FormGroup', () => {
        const formGroup = service.createConstructorEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            signature: expect.any(Object),
            pkg: expect.any(Object),
            file: expect.any(Object),
          }),
        );
      });
    });

    describe('getConstructorEntity', () => {
      it('should return NewConstructorEntity for default ConstructorEntity initial value', () => {
        const formGroup = service.createConstructorEntityFormGroup(sampleWithNewData);

        const constructorEntity = service.getConstructorEntity(formGroup) as any;

        expect(constructorEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewConstructorEntity for empty ConstructorEntity initial value', () => {
        const formGroup = service.createConstructorEntityFormGroup();

        const constructorEntity = service.getConstructorEntity(formGroup) as any;

        expect(constructorEntity).toMatchObject({});
      });

      it('should return IConstructorEntity', () => {
        const formGroup = service.createConstructorEntityFormGroup(sampleWithRequiredData);

        const constructorEntity = service.getConstructorEntity(formGroup) as any;

        expect(constructorEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConstructorEntity should not enable id FormControl', () => {
        const formGroup = service.createConstructorEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConstructorEntity should disable id FormControl', () => {
        const formGroup = service.createConstructorEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
