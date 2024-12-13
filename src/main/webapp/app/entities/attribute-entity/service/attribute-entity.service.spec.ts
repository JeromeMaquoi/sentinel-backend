import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAttributeEntity } from '../attribute-entity.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../attribute-entity.test-samples';

import { AttributeEntityService } from './attribute-entity.service';

const requireRestSample: IAttributeEntity = {
  ...sampleWithRequiredData,
};

describe('AttributeEntity Service', () => {
  let service: AttributeEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IAttributeEntity | IAttributeEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AttributeEntityService);
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

    it('should create a AttributeEntity', () => {
      const attributeEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(attributeEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AttributeEntity', () => {
      const attributeEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(attributeEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AttributeEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AttributeEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AttributeEntity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAttributeEntityToCollectionIfMissing', () => {
      it('should add a AttributeEntity to an empty array', () => {
        const attributeEntity: IAttributeEntity = sampleWithRequiredData;
        expectedResult = service.addAttributeEntityToCollectionIfMissing([], attributeEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attributeEntity);
      });

      it('should not add a AttributeEntity to an array that contains it', () => {
        const attributeEntity: IAttributeEntity = sampleWithRequiredData;
        const attributeEntityCollection: IAttributeEntity[] = [
          {
            ...attributeEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAttributeEntityToCollectionIfMissing(attributeEntityCollection, attributeEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AttributeEntity to an array that doesn't contain it", () => {
        const attributeEntity: IAttributeEntity = sampleWithRequiredData;
        const attributeEntityCollection: IAttributeEntity[] = [sampleWithPartialData];
        expectedResult = service.addAttributeEntityToCollectionIfMissing(attributeEntityCollection, attributeEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attributeEntity);
      });

      it('should add only unique AttributeEntity to an array', () => {
        const attributeEntityArray: IAttributeEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const attributeEntityCollection: IAttributeEntity[] = [sampleWithRequiredData];
        expectedResult = service.addAttributeEntityToCollectionIfMissing(attributeEntityCollection, ...attributeEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attributeEntity: IAttributeEntity = sampleWithRequiredData;
        const attributeEntity2: IAttributeEntity = sampleWithPartialData;
        expectedResult = service.addAttributeEntityToCollectionIfMissing([], attributeEntity, attributeEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attributeEntity);
        expect(expectedResult).toContain(attributeEntity2);
      });

      it('should accept null and undefined values', () => {
        const attributeEntity: IAttributeEntity = sampleWithRequiredData;
        expectedResult = service.addAttributeEntityToCollectionIfMissing([], null, attributeEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attributeEntity);
      });

      it('should return initial array if no AttributeEntity is added', () => {
        const attributeEntityCollection: IAttributeEntity[] = [sampleWithRequiredData];
        expectedResult = service.addAttributeEntityToCollectionIfMissing(attributeEntityCollection, undefined, null);
        expect(expectedResult).toEqual(attributeEntityCollection);
      });
    });

    describe('compareAttributeEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAttributeEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareAttributeEntity(entity1, entity2);
        const compareResult2 = service.compareAttributeEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareAttributeEntity(entity1, entity2);
        const compareResult2 = service.compareAttributeEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareAttributeEntity(entity1, entity2);
        const compareResult2 = service.compareAttributeEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
