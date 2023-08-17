import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CkEntityService } from '../service/ck-entity.service';

import { CkEntityComponent } from './ck-entity.component';

describe('CkEntity Management Component', () => {
  let comp: CkEntityComponent;
  let fixture: ComponentFixture<CkEntityComponent>;
  let service: CkEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'ck-entity', component: CkEntityComponent }]),
        HttpClientTestingModule,
        CkEntityComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CkEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CkEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CkEntityService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.ckEntities?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to ckEntityService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getCkEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCkEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
