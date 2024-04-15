import { IJoularNodeEntity, NewJoularNodeEntity } from './joular-node-entity.model';

export const sampleWithRequiredData: IJoularNodeEntity = {
  id: '4191b74d-bd1b-48ea-b6c6-5989e6439796',
};

export const sampleWithPartialData: IJoularNodeEntity = {
  id: 'f57a3b35-2084-491b-8ca9-fc608ea043d2',
  lineNumber: 3912,
};

export const sampleWithFullData: IJoularNodeEntity = {
  id: 'ff5bafef-19b0-4014-8996-fb273a650522',
  lineNumber: 30606,
  value: 24671.53,
};

export const sampleWithNewData: NewJoularNodeEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
