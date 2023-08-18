import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommitEntity } from '../commit-entity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../commit-entity.test-samples';

import { CommitEntityService } from './commit-entity.service';

const requireRestSample: ICommitEntity = {
  ...sampleWithRequiredData,
};

describe('CommitEntity Service', () => {
  let service: CommitEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommitEntity | ICommitEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommitEntityService);
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

    it('should create a CommitEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const commitEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commitEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommitEntity', () => {
      const commitEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commitEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommitEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommitEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CommitEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommitEntityToCollectionIfMissing', () => {
      it('should add a CommitEntity to an empty array', () => {
        const commitEntity: ICommitEntity = sampleWithRequiredData;
        expectedResult = service.addCommitEntityToCollectionIfMissing([], commitEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commitEntity);
      });

      it('should not add a CommitEntity to an array that contains it', () => {
        const commitEntity: ICommitEntity = sampleWithRequiredData;
        const commitEntityCollection: ICommitEntity[] = [
          {
            ...commitEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommitEntityToCollectionIfMissing(commitEntityCollection, commitEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommitEntity to an array that doesn't contain it", () => {
        const commitEntity: ICommitEntity = sampleWithRequiredData;
        const commitEntityCollection: ICommitEntity[] = [sampleWithPartialData];
        expectedResult = service.addCommitEntityToCollectionIfMissing(commitEntityCollection, commitEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commitEntity);
      });

      it('should add only unique CommitEntity to an array', () => {
        const commitEntityArray: ICommitEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commitEntityCollection: ICommitEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCommitEntityToCollectionIfMissing(commitEntityCollection, ...commitEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commitEntity: ICommitEntity = sampleWithRequiredData;
        const commitEntity2: ICommitEntity = sampleWithPartialData;
        expectedResult = service.addCommitEntityToCollectionIfMissing([], commitEntity, commitEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commitEntity);
        expect(expectedResult).toContain(commitEntity2);
      });

      it('should accept null and undefined values', () => {
        const commitEntity: ICommitEntity = sampleWithRequiredData;
        expectedResult = service.addCommitEntityToCollectionIfMissing([], null, commitEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commitEntity);
      });

      it('should return initial array if no CommitEntity is added', () => {
        const commitEntityCollection: ICommitEntity[] = [sampleWithRequiredData];
        expectedResult = service.addCommitEntityToCollectionIfMissing(commitEntityCollection, undefined, null);
        expect(expectedResult).toEqual(commitEntityCollection);
      });
    });

    describe('compareCommitEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommitEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCommitEntity(entity1, entity2);
        const compareResult2 = service.compareCommitEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCommitEntity(entity1, entity2);
        const compareResult2 = service.compareCommitEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCommitEntity(entity1, entity2);
        const compareResult2 = service.compareCommitEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
