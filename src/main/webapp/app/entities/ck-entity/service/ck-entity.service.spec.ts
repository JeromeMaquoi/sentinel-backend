import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICkEntity } from '../ck-entity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ck-entity.test-samples';

import { CkEntityService } from './ck-entity.service';

const requireRestSample: ICkEntity = {
  ...sampleWithRequiredData,
};

describe('CkEntity Service', () => {
  let service: CkEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: ICkEntity | ICkEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CkEntityService);
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

    it('should create a CkEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ckEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ckEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CkEntity', () => {
      const ckEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ckEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CkEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CkEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CkEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCkEntityToCollectionIfMissing', () => {
      it('should add a CkEntity to an empty array', () => {
        const ckEntity: ICkEntity = sampleWithRequiredData;
        expectedResult = service.addCkEntityToCollectionIfMissing([], ckEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ckEntity);
      });

      it('should not add a CkEntity to an array that contains it', () => {
        const ckEntity: ICkEntity = sampleWithRequiredData;
        const ckEntityCollection: ICkEntity[] = [
          {
            ...ckEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCkEntityToCollectionIfMissing(ckEntityCollection, ckEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CkEntity to an array that doesn't contain it", () => {
        const ckEntity: ICkEntity = sampleWithRequiredData;
        const ckEntityCollection: ICkEntity[] = [sampleWithPartialData];
        expectedResult = service.addCkEntityToCollectionIfMissing(ckEntityCollection, ckEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ckEntity);
      });

      it('should add only unique CkEntity to an array', () => {
        const ckEntityArray: ICkEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ckEntityCollection: ICkEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCkEntityToCollectionIfMissing(ckEntityCollection, ...ckEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ckEntity: ICkEntity = sampleWithRequiredData;
        const ckEntity2: ICkEntity = sampleWithPartialData;
        expectedResult = service.addCkEntityToCollectionIfMissing([], ckEntity, ckEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ckEntity);
        expect(expectedResult).toContain(ckEntity2);
      });

      it('should accept null and undefined values', () => {
        const ckEntity: ICkEntity = sampleWithRequiredData;
        expectedResult = service.addCkEntityToCollectionIfMissing([], null, ckEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ckEntity);
      });

      it('should return initial array if no CkEntity is added', () => {
        const ckEntityCollection: ICkEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCkEntityToCollectionIfMissing(ckEntityCollection, undefined, null);
        expect(expectedResult).toEqual(ckEntityCollection);
      });
    });

    describe('compareCkEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCkEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCkEntity(entity1, entity2);
        const compareResult2 = service.compareCkEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCkEntity(entity1, entity2);
        const compareResult2 = service.compareCkEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCkEntity(entity1, entity2);
        const compareResult2 = service.compareCkEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
