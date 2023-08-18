export interface ICommitEntity {
  id: string;
  sha?: string | null;
}

export type NewCommitEntity = Omit<ICommitEntity, 'id'> & { id: null };
