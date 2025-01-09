import { IStackTraceElementJoularEntity, NewStackTraceElementJoularEntity } from './stack-trace-element-joular-entity.model';

export const sampleWithRequiredData: IStackTraceElementJoularEntity = {
  id: '201f21e9-c301-46b5-af1f-fc688a2bb8c1',
};

export const sampleWithPartialData: IStackTraceElementJoularEntity = {
  id: 'b3e76c63-3aa6-4c91-9b13-e422f8dc8017',
  ancestors: 'notwithstanding but uh-huh',
  consumptionValues: 'hope vet throughout',
};

export const sampleWithFullData: IStackTraceElementJoularEntity = {
  id: '5f9b96c9-08d8-4d55-b945-df8ec135afb7',
  lineNumber: 28213,
  constructorElement: 'char er',
  parent: 'however',
  ancestors: 'vulgarise busily helpfully',
  consumptionValues: 'tired godfather reassuringly',
  commit: 'truly',
};

export const sampleWithNewData: NewStackTraceElementJoularEntity = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
