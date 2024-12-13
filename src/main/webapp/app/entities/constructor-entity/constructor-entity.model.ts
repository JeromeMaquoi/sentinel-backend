export interface IConstructorEntity {
  id: string;
  name?: string | null;
  signature?: string | null;
  pkg?: string | null;
  file?: string | null;
}

export type NewConstructorEntity = Omit<IConstructorEntity, 'id'> & { id: null };
