import { IAttributeEntity, NewAttributeEntity } from './attribute-entity.model';

export const sampleWithRequiredData: IAttributeEntity = {
  id: 'ad2e2d8b-289c-405c-b384-2e9808afedeb',
};

export const sampleWithPartialData: IAttributeEntity = {
  id: 'cce7f9bc-65bb-424e-a0f4-aa67dcdd1a59',
  name: 'awful pigsty uh-huh',
};

export const sampleWithFullData: IAttributeEntity = {
  id: '1e7ff0c0-6dbb-43ce-a63e-8f38cb3eb5c2',
  name: 'with option until',
  type: 'always',
};

export const sampleWithNewData: NewAttributeEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
