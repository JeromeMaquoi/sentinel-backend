import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JoularNodeEntityService } from '../service/joular-node-entity.service';
import { IJoularNodeEntity } from '../joular-node-entity.model';
import { JoularNodeEntityFormService } from './joular-node-entity-form.service';

import { JoularNodeEntityUpdateComponent } from './joular-node-entity-update.component';

describe('JoularNodeEntity Management Update Component', () => {
  let comp: JoularNodeEntityUpdateComponent;
  let fixture: ComponentFixture<JoularNodeEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let joularNodeEntityFormService: JoularNodeEntityFormService;
  let joularNodeEntityService: JoularNodeEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), JoularNodeEntityUpdateComponent],
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
      .overrideTemplate(JoularNodeEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JoularNodeEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    joularNodeEntityFormService = TestBed.inject(JoularNodeEntityFormService);
    joularNodeEntityService = TestBed.inject(JoularNodeEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const joularNodeEntity: IJoularNodeEntity = { id: 'CBA' };

      activatedRoute.data = of({ joularNodeEntity });
      comp.ngOnInit();

      expect(comp.joularNodeEntity).toEqual(joularNodeEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJoularNodeEntity>>();
      const joularNodeEntity = { id: 'ABC' };
      jest.spyOn(joularNodeEntityFormService, 'getJoularNodeEntity').mockReturnValue(joularNodeEntity);
      jest.spyOn(joularNodeEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ joularNodeEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: joularNodeEntity }));
      saveSubject.complete();

      // THEN
      expect(joularNodeEntityFormService.getJoularNodeEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(joularNodeEntityService.update).toHaveBeenCalledWith(expect.objectContaining(joularNodeEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJoularNodeEntity>>();
      const joularNodeEntity = { id: 'ABC' };
      jest.spyOn(joularNodeEntityFormService, 'getJoularNodeEntity').mockReturnValue({ id: null });
      jest.spyOn(joularNodeEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ joularNodeEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: joularNodeEntity }));
      saveSubject.complete();

      // THEN
      expect(joularNodeEntityFormService.getJoularNodeEntity).toHaveBeenCalled();
      expect(joularNodeEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJoularNodeEntity>>();
      const joularNodeEntity = { id: 'ABC' };
      jest.spyOn(joularNodeEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ joularNodeEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(joularNodeEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
