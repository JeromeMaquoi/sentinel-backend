import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { JoularNodeEntityService } from '../service/joular-node-entity.service';

import { JoularNodeEntityComponent } from './joular-node-entity.component';

describe('JoularNodeEntity Management Component', () => {
  let comp: JoularNodeEntityComponent;
  let fixture: ComponentFixture<JoularNodeEntityComponent>;
  let service: JoularNodeEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'joular-node-entity', component: JoularNodeEntityComponent }]),
        HttpClientTestingModule,
        JoularNodeEntityComponent,
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
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(JoularNodeEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JoularNodeEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(JoularNodeEntityService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.joularNodeEntities?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to joularNodeEntityService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getJoularNodeEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getJoularNodeEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
