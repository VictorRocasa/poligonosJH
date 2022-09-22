import { IForma, NewForma } from './forma.model';

export const sampleWithRequiredData: IForma = {
  id: 30983,
};

export const sampleWithPartialData: IForma = {
  id: 43169,
};

export const sampleWithFullData: IForma = {
  id: 9748,
};

export const sampleWithNewData: NewForma = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
