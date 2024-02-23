export interface IJoularNodeEntity {
  id: string;
  lineNumber?: number | null;
  value?: number | null;
}

export type NewJoularNodeEntity = Omit<IJoularNodeEntity, 'id'> & { id: null };
