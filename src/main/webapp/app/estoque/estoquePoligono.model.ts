export interface IEstoquePoligonos {
  lados: number | null;
  tamanho: number | null;
  ocorrencias: number | null;
}

export type NewPoligono = Omit<IEstoquePoligonos, 'id'> & { id: null };
