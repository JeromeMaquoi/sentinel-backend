import { IConstructorEntity } from 'app/entities/constructor-entity/constructor-entity.model';

export interface IAttributeEntity {
  id: string;
  name?: string | null;
  type?: string | null;
  constructorEntity?: IConstructorEntity | null;
}

export type NewAttributeEntity = Omit<IAttributeEntity, 'id'> & { id: null };
