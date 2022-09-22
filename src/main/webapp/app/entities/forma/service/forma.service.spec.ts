import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IForma } from '../forma.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../forma.test-samples';

import { FormaService } from './forma.service';

const requireRestSample: IForma = {
  ...sampleWithRequiredData,
};

describe('Forma Service', () => {
  let service: FormaService;
  let httpMock: HttpTestingController;
  let expectedResult: IForma | IForma[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormaService);
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

    it('should create a Forma', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const forma = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(forma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Forma', () => {
      const forma = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(forma).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Forma', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Forma', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Forma', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFormaToCollectionIfMissing', () => {
      it('should add a Forma to an empty array', () => {
        const forma: IForma = sampleWithRequiredData;
        expectedResult = service.addFormaToCollectionIfMissing([], forma);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(forma);
      });

      it('should not add a Forma to an array that contains it', () => {
        const forma: IForma = sampleWithRequiredData;
        const formaCollection: IForma[] = [
          {
            ...forma,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormaToCollectionIfMissing(formaCollection, forma);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Forma to an array that doesn't contain it", () => {
        const forma: IForma = sampleWithRequiredData;
        const formaCollection: IForma[] = [sampleWithPartialData];
        expectedResult = service.addFormaToCollectionIfMissing(formaCollection, forma);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(forma);
      });

      it('should add only unique Forma to an array', () => {
        const formaArray: IForma[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formaCollection: IForma[] = [sampleWithRequiredData];
        expectedResult = service.addFormaToCollectionIfMissing(formaCollection, ...formaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const forma: IForma = sampleWithRequiredData;
        const forma2: IForma = sampleWithPartialData;
        expectedResult = service.addFormaToCollectionIfMissing([], forma, forma2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(forma);
        expect(expectedResult).toContain(forma2);
      });

      it('should accept null and undefined values', () => {
        const forma: IForma = sampleWithRequiredData;
        expectedResult = service.addFormaToCollectionIfMissing([], null, forma, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(forma);
      });

      it('should return initial array if no Forma is added', () => {
        const formaCollection: IForma[] = [sampleWithRequiredData];
        expectedResult = service.addFormaToCollectionIfMissing(formaCollection, undefined, null);
        expect(expectedResult).toEqual(formaCollection);
      });
    });

    describe('compareForma', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareForma(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareForma(entity1, entity2);
        const compareResult2 = service.compareForma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareForma(entity1, entity2);
        const compareResult2 = service.compareForma(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareForma(entity1, entity2);
        const compareResult2 = service.compareForma(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
