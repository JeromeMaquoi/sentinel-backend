import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import { StackTraceElementJoularEntityService } from '../service/stack-trace-element-joular-entity.service';

import stackTraceElementJoularEntityResolve from './stack-trace-element-joular-entity-routing-resolve.service';

describe('StackTraceElementJoularEntity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: StackTraceElementJoularEntityService;
  let resultStackTraceElementJoularEntity: IStackTraceElementJoularEntity | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(StackTraceElementJoularEntityService);
    resultStackTraceElementJoularEntity = undefined;
  });

  describe('resolve', () => {
    it('should return IStackTraceElementJoularEntity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        stackTraceElementJoularEntityResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStackTraceElementJoularEntity = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultStackTraceElementJoularEntity).toEqual({ id: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        stackTraceElementJoularEntityResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStackTraceElementJoularEntity = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultStackTraceElementJoularEntity).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IStackTraceElementJoularEntity>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        stackTraceElementJoularEntityResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultStackTraceElementJoularEntity = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultStackTraceElementJoularEntity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
