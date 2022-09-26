import { IForma } from 'app/entities/forma/forma.model';
import { IEstoquePoligonos } from './estoquePoligono.model';

export interface IEstoque {
  poligonos?: IEstoquePoligonos[] | null;
  formas?: IForma[] | null;
}
