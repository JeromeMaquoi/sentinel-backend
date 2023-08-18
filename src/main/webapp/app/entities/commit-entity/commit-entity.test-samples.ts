import { ICommitEntity, NewCommitEntity } from './commit-entity.model';

export const sampleWithRequiredData: ICommitEntity = {
  id: '7c637ced-e085-4d67-b825-e25cce57ba34',
  sha: 'Health',
};

export const sampleWithPartialData: ICommitEntity = {
  id: 'b891de5e-8daa-4f3e-9204-4e0ebed73cba',
  sha: 'Keyboard area',
};

export const sampleWithFullData: ICommitEntity = {
  id: 'fe505dde-8ea0-4c8c-8946-4ba6a3181c8b',
  sha: 'Funk Stokes',
};

export const sampleWithNewData: NewCommitEntity = {
  sha: 'Cargo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
