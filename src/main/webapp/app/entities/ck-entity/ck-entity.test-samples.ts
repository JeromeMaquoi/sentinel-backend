import { ICkEntity, NewCkEntity } from './ck-entity.model';

export const sampleWithRequiredData: ICkEntity = {
  id: 'f3eecf90-8503-4368-aba8-428c028361b9',
  name: 'Soft eveniet Schaefer',
  value: 16250,
  tool_version: 'turquoise Blues Denar',
};

export const sampleWithPartialData: ICkEntity = {
  id: '689920b0-28e2-4bca-80e7-63064e25fab8',
  name: 'indigo',
  value: 6217,
  tool_version: 'Gasoline',
};

export const sampleWithFullData: ICkEntity = {
  id: '2fd483f4-6fde-48e6-8aec-9b71ba5f9aeb',
  name: 'Refined Gasoline',
  value: 16527,
  tool_version: 'Cab Kentucky Meitnerium',
};

export const sampleWithNewData: NewCkEntity = {
  name: 'encode',
  value: 29074,
  tool_version: 'Jewelery circa Einsteinium',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
