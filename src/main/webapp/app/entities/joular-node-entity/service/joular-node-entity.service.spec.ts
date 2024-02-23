import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJoularNodeEntity } from '../joular-node-entity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../joular-node-entity.test-samples';

import { JoularNodeEntityService } from './joular-node-entity.service';

const requireRestSample: IJoularNodeEntity = {
  ...sampleWithRequiredData,
};

describe('JoularNodeEntity Service', () => {
  let service: JoularNodeEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IJoularNodeEntity | IJoularNodeEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JoularNodeEntityService);
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

    it('should create a JoularNodeEntity', () => {
      const joularNodeEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(joularNodeEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JoularNodeEntity', () => {
      const joularNodeEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(joularNodeEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JoularNodeEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JoularNodeEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a JoularNodeEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addJoularNodeEntityToCollectionIfMissing', () => {
      it('should add a JoularNodeEntity to an empty array', () => {
        const joularNodeEntity: IJoularNodeEntity = sampleWithRequiredData;
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing([], joularNodeEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(joularNodeEntity);
      });

      it('should not add a JoularNodeEntity to an array that contains it', () => {
        const joularNodeEntity: IJoularNodeEntity = sampleWithRequiredData;
        const joularNodeEntityCollection: IJoularNodeEntity[] = [
          {
            ...joularNodeEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing(joularNodeEntityCollection, joularNodeEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JoularNodeEntity to an array that doesn't contain it", () => {
        const joularNodeEntity: IJoularNodeEntity = sampleWithRequiredData;
        const joularNodeEntityCollection: IJoularNodeEntity[] = [sampleWithPartialData];
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing(joularNodeEntityCollection, joularNodeEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(joularNodeEntity);
      });

      it('should add only unique JoularNodeEntity to an array', () => {
        const joularNodeEntityArray: IJoularNodeEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const joularNodeEntityCollection: IJoularNodeEntity[] = [sampleWithRequiredData];
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing(joularNodeEntityCollection, ...joularNodeEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const joularNodeEntity: IJoularNodeEntity = sampleWithRequiredData;
        const joularNodeEntity2: IJoularNodeEntity = sampleWithPartialData;
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing([], joularNodeEntity, joularNodeEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(joularNodeEntity);
        expect(expectedResult).toContain(joularNodeEntity2);
      });

      it('should accept null and undefined values', () => {
        const joularNodeEntity: IJoularNodeEntity = sampleWithRequiredData;
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing([], null, joularNodeEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(joularNodeEntity);
      });

      it('should return initial array if no JoularNodeEntity is added', () => {
        const joularNodeEntityCollection: IJoularNodeEntity[] = [sampleWithRequiredData];
        expectedResult = service.addJoularNodeEntityToCollectionIfMissing(joularNodeEntityCollection, undefined, null);
        expect(expectedResult).toEqual(joularNodeEntityCollection);
      });
    });

    describe('compareJoularNodeEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareJoularNodeEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareJoularNodeEntity(entity1, entity2);
        const compareResult2 = service.compareJoularNodeEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareJoularNodeEntity(entity1, entity2);
        const compareResult2 = service.compareJoularNodeEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareJoularNodeEntity(entity1, entity2);
        const compareResult2 = service.compareJoularNodeEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
