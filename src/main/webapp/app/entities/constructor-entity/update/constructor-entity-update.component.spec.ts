import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ConstructorEntityService } from '../service/constructor-entity.service';
import { IConstructorEntity } from '../constructor-entity.model';
import { ConstructorEntityFormService } from './constructor-entity-form.service';

import { ConstructorEntityUpdateComponent } from './constructor-entity-update.component';

describe('ConstructorEntity Management Update Component', () => {
  let comp: ConstructorEntityUpdateComponent;
  let fixture: ComponentFixture<ConstructorEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let constructorEntityFormService: ConstructorEntityFormService;
  let constructorEntityService: ConstructorEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConstructorEntityUpdateComponent],
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
      .overrideTemplate(ConstructorEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConstructorEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    constructorEntityFormService = TestBed.inject(ConstructorEntityFormService);
    constructorEntityService = TestBed.inject(ConstructorEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const constructorEntity: IConstructorEntity = { id: 'CBA' };

      activatedRoute.data = of({ constructorEntity });
      comp.ngOnInit();

      expect(comp.constructorEntity).toEqual(constructorEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConstructorEntity>>();
      const constructorEntity = { id: 'ABC' };
      jest.spyOn(constructorEntityFormService, 'getConstructorEntity').mockReturnValue(constructorEntity);
      jest.spyOn(constructorEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constructorEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: constructorEntity }));
      saveSubject.complete();

      // THEN
      expect(constructorEntityFormService.getConstructorEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(constructorEntityService.update).toHaveBeenCalledWith(expect.objectContaining(constructorEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConstructorEntity>>();
      const constructorEntity = { id: 'ABC' };
      jest.spyOn(constructorEntityFormService, 'getConstructorEntity').mockReturnValue({ id: null });
      jest.spyOn(constructorEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constructorEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: constructorEntity }));
      saveSubject.complete();

      // THEN
      expect(constructorEntityFormService.getConstructorEntity).toHaveBeenCalled();
      expect(constructorEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConstructorEntity>>();
      const constructorEntity = { id: 'ABC' };
      jest.spyOn(constructorEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ constructorEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(constructorEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
