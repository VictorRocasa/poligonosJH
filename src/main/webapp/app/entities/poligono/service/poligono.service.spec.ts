import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPoligono } from '../poligono.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../poligono.test-samples';

import { PoligonoService, RestPoligono } from './poligono.service';

const requireRestSample: RestPoligono = {
  ...sampleWithRequiredData,
  dataCriacao: sampleWithRequiredData.dataCriacao?.toJSON(),
  ultimaModificacao: sampleWithRequiredData.ultimaModificacao?.toJSON(),
};

describe('Poligono Service', () => {
  let service: PoligonoService;
  let httpMock: HttpTestingController;
  let expectedResult: IPoligono | IPoligono[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PoligonoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Poligono', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const poligono = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(poligono).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Poligono', () => {
      const poligono = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(poligono).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Poligono', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Poligono', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Poligono', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPoligonoToCollectionIfMissing', () => {
      it('should add a Poligono to an empty array', () => {
        const poligono: IPoligono = sampleWithRequiredData;
        expectedResult = service.addPoligonoToCollectionIfMissing([], poligono);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poligono);
      });

      it('should not add a Poligono to an array that contains it', () => {
        const poligono: IPoligono = sampleWithRequiredData;
        const poligonoCollection: IPoligono[] = [
          {
            ...poligono,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPoligonoToCollectionIfMissing(poligonoCollection, poligono);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Poligono to an array that doesn't contain it", () => {
        const poligono: IPoligono = sampleWithRequiredData;
        const poligonoCollection: IPoligono[] = [sampleWithPartialData];
        expectedResult = service.addPoligonoToCollectionIfMissing(poligonoCollection, poligono);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poligono);
      });

      it('should add only unique Poligono to an array', () => {
        const poligonoArray: IPoligono[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const poligonoCollection: IPoligono[] = [sampleWithRequiredData];
        expectedResult = service.addPoligonoToCollectionIfMissing(poligonoCollection, ...poligonoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const poligono: IPoligono = sampleWithRequiredData;
        const poligono2: IPoligono = sampleWithPartialData;
        expectedResult = service.addPoligonoToCollectionIfMissing([], poligono, poligono2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poligono);
        expect(expectedResult).toContain(poligono2);
      });

      it('should accept null and undefined values', () => {
        const poligono: IPoligono = sampleWithRequiredData;
        expectedResult = service.addPoligonoToCollectionIfMissing([], null, poligono, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poligono);
      });

      it('should return initial array if no Poligono is added', () => {
        const poligonoCollection: IPoligono[] = [sampleWithRequiredData];
        expectedResult = service.addPoligonoToCollectionIfMissing(poligonoCollection, undefined, null);
        expect(expectedResult).toEqual(poligonoCollection);
      });
    });

    describe('comparePoligono', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePoligono(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePoligono(entity1, entity2);
        const compareResult2 = service.comparePoligono(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePoligono(entity1, entity2);
        const compareResult2 = service.comparePoligono(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePoligono(entity1, entity2);
        const compareResult2 = service.comparePoligono(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
