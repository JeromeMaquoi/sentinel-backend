import { IConstructorEntity, NewConstructorEntity } from './constructor-entity.model';

export const sampleWithRequiredData: IConstructorEntity = {
  id: '81245409-c472-40de-a29c-b6e209cd0d70',
};

export const sampleWithPartialData: IConstructorEntity = {
  id: '06766ab6-dee7-4934-919b-b569fb2543e6',
  pkg: 'round comb pillbox',
};

export const sampleWithFullData: IConstructorEntity = {
  id: 'd40143fa-b9c8-4692-9174-d57dc0f2eac3',
  name: 'seldom pronoun browse',
  signature: 'fooey',
  pkg: 'against forswear',
  file: 'as lace gee',
};

export const sampleWithNewData: NewConstructorEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
