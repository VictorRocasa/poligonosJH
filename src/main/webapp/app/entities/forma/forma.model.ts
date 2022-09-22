export interface IForma {
  id: number;
  agrupamento?: Pick<IForma, 'id'> | null;
}

export type NewForma = Omit<IForma, 'id'> & { id: null };
