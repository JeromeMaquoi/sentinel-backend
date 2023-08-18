import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommitEntityFormService } from './commit-entity-form.service';
import { CommitEntityService } from '../service/commit-entity.service';
import { ICommitEntity } from '../commit-entity.model';

import { CommitEntityUpdateComponent } from './commit-entity-update.component';

describe('CommitEntity Management Update Component', () => {
  let comp: CommitEntityUpdateComponent;
  let fixture: ComponentFixture<CommitEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commitEntityFormService: CommitEntityFormService;
  let commitEntityService: CommitEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CommitEntityUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommitEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommitEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commitEntityFormService = TestBed.inject(CommitEntityFormService);
    commitEntityService = TestBed.inject(CommitEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const commitEntity: ICommitEntity = { id: 'CBA' };

      activatedRoute.data = of({ commitEntity });
      comp.ngOnInit();

      expect(comp.commitEntity).toEqual(commitEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommitEntity>>();
      const commitEntity = { id: 'ABC' };
      jest.spyOn(commitEntityFormService, 'getCommitEntity').mockReturnValue(commitEntity);
      jest.spyOn(commitEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commitEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commitEntity }));
      saveSubject.complete();

      // THEN
      expect(commitEntityFormService.getCommitEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commitEntityService.update).toHaveBeenCalledWith(expect.objectContaining(commitEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommitEntity>>();
      const commitEntity = { id: 'ABC' };
      jest.spyOn(commitEntityFormService, 'getCommitEntity').mockReturnValue({ id: null });
      jest.spyOn(commitEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commitEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commitEntity }));
      saveSubject.complete();

      // THEN
      expect(commitEntityFormService.getCommitEntity).toHaveBeenCalled();
      expect(commitEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommitEntity>>();
      const commitEntity = { id: 'ABC' };
      jest.spyOn(commitEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commitEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commitEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
