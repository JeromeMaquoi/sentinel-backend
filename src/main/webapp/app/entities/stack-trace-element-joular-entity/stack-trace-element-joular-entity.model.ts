export interface IStackTraceElementJoularEntity {
  id: string;
  lineNumber?: number | null;
  constructorElement?: string | null;
  parent?: string | null;
  ancestors?: string | null;
  consumptionValues?: string | null;
  commit?: string | null;
}

export type NewStackTraceElementJoularEntity = Omit<IStackTraceElementJoularEntity, 'id'> & { id: null };
