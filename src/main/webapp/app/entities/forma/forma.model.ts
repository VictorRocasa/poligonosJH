import { IPoligono } from '../poligono/poligono.model';

export interface IForma {
  id: number;
  agrupamento?: Pick<IForma, 'id'> | null;
  formas?: IForma[] | null;
  poligonos?: IPoligono[] | null;
}

export type NewForma = Omit<IForma, 'id'> & { id: null };
