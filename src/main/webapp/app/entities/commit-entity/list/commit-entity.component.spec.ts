import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CommitEntityService } from '../service/commit-entity.service';

import { CommitEntityComponent } from './commit-entity.component';

describe('CommitEntity Management Component', () => {
  let comp: CommitEntityComponent;
  let fixture: ComponentFixture<CommitEntityComponent>;
  let service: CommitEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'commit-entity', component: CommitEntityComponent }]),
        HttpClientTestingModule,
        CommitEntityComponent,
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
      .overrideTemplate(CommitEntityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommitEntityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CommitEntityService);

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
    expect(comp.commitEntities?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to commitEntityService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getCommitEntityIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCommitEntityIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
