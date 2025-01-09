import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../stack-trace-element-joular-entity.test-samples';

import { StackTraceElementJoularEntityService } from './stack-trace-element-joular-entity.service';

const requireRestSample: IStackTraceElementJoularEntity = {
  ...sampleWithRequiredData,
};

describe('StackTraceElementJoularEntity Service', () => {
  let service: StackTraceElementJoularEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IStackTraceElementJoularEntity | IStackTraceElementJoularEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StackTraceElementJoularEntityService);
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

    it('should create a StackTraceElementJoularEntity', () => {
      const stackTraceElementJoularEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stackTraceElementJoularEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StackTraceElementJoularEntity', () => {
      const stackTraceElementJoularEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stackTraceElementJoularEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StackTraceElementJoularEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StackTraceElementJoularEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StackTraceElementJoularEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStackTraceElementJoularEntityToCollectionIfMissing', () => {
      it('should add a StackTraceElementJoularEntity to an empty array', () => {
        const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = sampleWithRequiredData;
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing([], stackTraceElementJoularEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stackTraceElementJoularEntity);
      });

      it('should not add a StackTraceElementJoularEntity to an array that contains it', () => {
        const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = sampleWithRequiredData;
        const stackTraceElementJoularEntityCollection: IStackTraceElementJoularEntity[] = [
          {
            ...stackTraceElementJoularEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing(
          stackTraceElementJoularEntityCollection,
          stackTraceElementJoularEntity,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StackTraceElementJoularEntity to an array that doesn't contain it", () => {
        const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = sampleWithRequiredData;
        const stackTraceElementJoularEntityCollection: IStackTraceElementJoularEntity[] = [sampleWithPartialData];
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing(
          stackTraceElementJoularEntityCollection,
          stackTraceElementJoularEntity,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stackTraceElementJoularEntity);
      });

      it('should add only unique StackTraceElementJoularEntity to an array', () => {
        const stackTraceElementJoularEntityArray: IStackTraceElementJoularEntity[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const stackTraceElementJoularEntityCollection: IStackTraceElementJoularEntity[] = [sampleWithRequiredData];
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing(
          stackTraceElementJoularEntityCollection,
          ...stackTraceElementJoularEntityArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = sampleWithRequiredData;
        const stackTraceElementJoularEntity2: IStackTraceElementJoularEntity = sampleWithPartialData;
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing(
          [],
          stackTraceElementJoularEntity,
          stackTraceElementJoularEntity2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stackTraceElementJoularEntity);
        expect(expectedResult).toContain(stackTraceElementJoularEntity2);
      });

      it('should accept null and undefined values', () => {
        const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = sampleWithRequiredData;
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing([], null, stackTraceElementJoularEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stackTraceElementJoularEntity);
      });

      it('should return initial array if no StackTraceElementJoularEntity is added', () => {
        const stackTraceElementJoularEntityCollection: IStackTraceElementJoularEntity[] = [sampleWithRequiredData];
        expectedResult = service.addStackTraceElementJoularEntityToCollectionIfMissing(
          stackTraceElementJoularEntityCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(stackTraceElementJoularEntityCollection);
      });
    });

    describe('compareStackTraceElementJoularEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStackTraceElementJoularEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareStackTraceElementJoularEntity(entity1, entity2);
        const compareResult2 = service.compareStackTraceElementJoularEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareStackTraceElementJoularEntity(entity1, entity2);
        const compareResult2 = service.compareStackTraceElementJoularEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareStackTraceElementJoularEntity(entity1, entity2);
        const compareResult2 = service.compareStackTraceElementJoularEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
