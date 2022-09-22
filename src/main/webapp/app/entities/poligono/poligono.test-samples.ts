import dayjs from 'dayjs/esm';

import { IPoligono, NewPoligono } from './poligono.model';

export const sampleWithRequiredData: IPoligono = {
  id: 7577,
  lados: 9358,
  tamanho: 8636,
  dataCriacao: dayjs('2022-09-20T23:53'),
  ultimaModificacao: dayjs('2022-09-21T14:42'),
};

export const sampleWithPartialData: IPoligono = {
  id: 16421,
  lados: 87208,
  tamanho: 93747,
  dataCriacao: dayjs('2022-09-21T08:22'),
  ultimaModificacao: dayjs('2022-09-21T08:51'),
};

export const sampleWithFullData: IPoligono = {
  id: 63385,
  lados: 67660,
  tamanho: 6641,
  dataCriacao: dayjs('2022-09-21T18:20'),
  ultimaModificacao: dayjs('2022-09-21T13:14'),
};

export const sampleWithNewData: NewPoligono = {
  lados: 49080,
  tamanho: 6194,
  dataCriacao: dayjs('2022-09-21T09:46'),
  ultimaModificacao: dayjs('2022-09-20T20:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
