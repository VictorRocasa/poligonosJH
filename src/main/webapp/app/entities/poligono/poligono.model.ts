import dayjs from 'dayjs/esm';
import { IForma } from 'app/entities/forma/forma.model';

export interface IPoligono {
  id: number;
  lados?: number | null;
  tamanho?: number | null;
  dataCriacao?: dayjs.Dayjs | null;
  ultimaModificacao?: dayjs.Dayjs | null;
  forma?: Pick<IForma, 'id'> | null;
}

export type NewPoligono = Omit<IPoligono, 'id'> & { id: null };
