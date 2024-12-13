import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../attribute-entity.test-samples';

import { AttributeEntityFormService } from './attribute-entity-form.service';

describe('AttributeEntity Form Service', () => {
  let service: AttributeEntityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttributeEntityFormService);
  });

  describe('Service methods', () => {
    describe('createAttributeEntityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttributeEntityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            constructorEntity: expect.any(Object),
          }),
        );
      });

      it('passing IAttributeEntity should create a new form with FormGroup', () => {
        const formGroup = service.createAttributeEntityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            constructorEntity: expect.any(Object),
          }),
        );
      });
    });

    describe('getAttributeEntity', () => {
      it('should return NewAttributeEntity for default AttributeEntity initial value', () => {
        const formGroup = service.createAttributeEntityFormGroup(sampleWithNewData);

        const attributeEntity = service.getAttributeEntity(formGroup) as any;

        expect(attributeEntity).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttributeEntity for empty AttributeEntity initial value', () => {
        const formGroup = service.createAttributeEntityFormGroup();

        const attributeEntity = service.getAttributeEntity(formGroup) as any;

        expect(attributeEntity).toMatchObject({});
      });

      it('should return IAttributeEntity', () => {
        const formGroup = service.createAttributeEntityFormGroup(sampleWithRequiredData);

        const attributeEntity = service.getAttributeEntity(formGroup) as any;

        expect(attributeEntity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttributeEntity should not enable id FormControl', () => {
        const formGroup = service.createAttributeEntityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttributeEntity should disable id FormControl', () => {
        const formGroup = service.createAttributeEntityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
