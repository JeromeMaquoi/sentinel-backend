import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IConstructorEntity } from '../constructor-entity.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../constructor-entity.test-samples';

import { ConstructorEntityService } from './constructor-entity.service';

const requireRestSample: IConstructorEntity = {
  ...sampleWithRequiredData,
};

describe('ConstructorEntity Service', () => {
  let service: ConstructorEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IConstructorEntity | IConstructorEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ConstructorEntityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ConstructorEntity', () => {
      const constructorEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(constructorEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConstructorEntity', () => {
      const constructorEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(constructorEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConstructorEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConstructorEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConstructorEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConstructorEntityToCollectionIfMissing', () => {
      it('should add a ConstructorEntity to an empty array', () => {
        const constructorEntity: IConstructorEntity = sampleWithRequiredData;
        expectedResult = service.addConstructorEntityToCollectionIfMissing([], constructorEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(constructorEntity);
      });

      it('should not add a ConstructorEntity to an array that contains it', () => {
        const constructorEntity: IConstructorEntity = sampleWithRequiredData;
        const constructorEntityCollection: IConstructorEntity[] = [
          {
            ...constructorEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConstructorEntityToCollectionIfMissing(constructorEntityCollection, constructorEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConstructorEntity to an array that doesn't contain it", () => {
        const constructorEntity: IConstructorEntity = sampleWithRequiredData;
        const constructorEntityCollection: IConstructorEntity[] = [sampleWithPartialData];
        expectedResult = service.addConstructorEntityToCollectionIfMissing(constructorEntityCollection, constructorEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(constructorEntity);
      });

      it('should add only unique ConstructorEntity to an array', () => {
        const constructorEntityArray: IConstructorEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const constructorEntityCollection: IConstructorEntity[] = [sampleWithRequiredData];
        expectedResult = service.addConstructorEntityToCollectionIfMissing(constructorEntityCollection, ...constructorEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const constructorEntity: IConstructorEntity = sampleWithRequiredData;
        const constructorEntity2: IConstructorEntity = sampleWithPartialData;
        expectedResult = service.addConstructorEntityToCollectionIfMissing([], constructorEntity, constructorEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(constructorEntity);
        expect(expectedResult).toContain(constructorEntity2);
      });

      it('should accept null and undefined values', () => {
        const constructorEntity: IConstructorEntity = sampleWithRequiredData;
        expectedResult = service.addConstructorEntityToCollectionIfMissing([], null, constructorEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(constructorEntity);
      });

      it('should return initial array if no ConstructorEntity is added', () => {
        const constructorEntityCollection: IConstructorEntity[] = [sampleWithRequiredData];
        expectedResult = service.addConstructorEntityToCollectionIfMissing(constructorEntityCollection, undefined, null);
        expect(expectedResult).toEqual(constructorEntityCollection);
      });
    });

    describe('compareConstructorEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConstructorEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareConstructorEntity(entity1, entity2);
        const compareResult2 = service.compareConstructorEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareConstructorEntity(entity1, entity2);
        const compareResult2 = service.compareConstructorEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareConstructorEntity(entity1, entity2);
        const compareResult2 = service.compareConstructorEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
