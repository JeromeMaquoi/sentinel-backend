import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { StackTraceElementJoularEntityService } from '../service/stack-trace-element-joular-entity.service';
import { IStackTraceElementJoularEntity } from '../stack-trace-element-joular-entity.model';
import { StackTraceElementJoularEntityFormService } from './stack-trace-element-joular-entity-form.service';

import { StackTraceElementJoularEntityUpdateComponent } from './stack-trace-element-joular-entity-update.component';

describe('StackTraceElementJoularEntity Management Update Component', () => {
  let comp: StackTraceElementJoularEntityUpdateComponent;
  let fixture: ComponentFixture<StackTraceElementJoularEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stackTraceElementJoularEntityFormService: StackTraceElementJoularEntityFormService;
  let stackTraceElementJoularEntityService: StackTraceElementJoularEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StackTraceElementJoularEntityUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StackTraceElementJoularEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StackTraceElementJoularEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stackTraceElementJoularEntityFormService = TestBed.inject(StackTraceElementJoularEntityFormService);
    stackTraceElementJoularEntityService = TestBed.inject(StackTraceElementJoularEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stackTraceElementJoularEntity: IStackTraceElementJoularEntity = { id: 'CBA' };

      activatedRoute.data = of({ stackTraceElementJoularEntity });
      comp.ngOnInit();

      expect(comp.stackTraceElementJoularEntity).toEqual(stackTraceElementJoularEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStackTraceElementJoularEntity>>();
      const stackTraceElementJoularEntity = { id: 'ABC' };
      jest
        .spyOn(stackTraceElementJoularEntityFormService, 'getStackTraceElementJoularEntity')
        .mockReturnValue(stackTraceElementJoularEntity);
      jest.spyOn(stackTraceElementJoularEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stackTraceElementJoularEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stackTraceElementJoularEntity }));
      saveSubject.complete();

      // THEN
      expect(stackTraceElementJoularEntityFormService.getStackTraceElementJoularEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stackTraceElementJoularEntityService.update).toHaveBeenCalledWith(expect.objectContaining(stackTraceElementJoularEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStackTraceElementJoularEntity>>();
      const stackTraceElementJoularEntity = { id: 'ABC' };
      jest.spyOn(stackTraceElementJoularEntityFormService, 'getStackTraceElementJoularEntity').mockReturnValue({ id: null });
      jest.spyOn(stackTraceElementJoularEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stackTraceElementJoularEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stackTraceElementJoularEntity }));
      saveSubject.complete();

      // THEN
      expect(stackTraceElementJoularEntityFormService.getStackTraceElementJoularEntity).toHaveBeenCalled();
      expect(stackTraceElementJoularEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStackTraceElementJoularEntity>>();
      const stackTraceElementJoularEntity = { id: 'ABC' };
      jest.spyOn(stackTraceElementJoularEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stackTraceElementJoularEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stackTraceElementJoularEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
