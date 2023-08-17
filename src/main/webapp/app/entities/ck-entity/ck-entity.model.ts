export interface ICkEntity {
  id: string;
  name?: string | null;
  value?: number | null;
  tool_version?: string | null;
}

export type NewCkEntity = Omit<ICkEntity, 'id'> & { id: null };
