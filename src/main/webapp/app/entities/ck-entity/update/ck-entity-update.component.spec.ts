import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CkEntityFormService } from './ck-entity-form.service';
import { CkEntityService } from '../service/ck-entity.service';
import { ICkEntity } from '../ck-entity.model';

import { CkEntityUpdateComponent } from './ck-entity-update.component';

describe('CkEntity Management Update Component', () => {
  let comp: CkEntityUpdateComponent;
  let fixture: ComponentFixture<CkEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ckEntityFormService: CkEntityFormService;
  let ckEntityService: CkEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CkEntityUpdateComponent],
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
      .overrideTemplate(CkEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CkEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ckEntityFormService = TestBed.inject(CkEntityFormService);
    ckEntityService = TestBed.inject(CkEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ckEntity: ICkEntity = { id: 'CBA' };

      activatedRoute.data = of({ ckEntity });
      comp.ngOnInit();

      expect(comp.ckEntity).toEqual(ckEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICkEntity>>();
      const ckEntity = { id: 'ABC' };
      jest.spyOn(ckEntityFormService, 'getCkEntity').mockReturnValue(ckEntity);
      jest.spyOn(ckEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ckEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ckEntity }));
      saveSubject.complete();

      // THEN
      expect(ckEntityFormService.getCkEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ckEntityService.update).toHaveBeenCalledWith(expect.objectContaining(ckEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICkEntity>>();
      const ckEntity = { id: 'ABC' };
      jest.spyOn(ckEntityFormService, 'getCkEntity').mockReturnValue({ id: null });
      jest.spyOn(ckEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ckEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ckEntity }));
      saveSubject.complete();

      // THEN
      expect(ckEntityFormService.getCkEntity).toHaveBeenCalled();
      expect(ckEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICkEntity>>();
      const ckEntity = { id: 'ABC' };
      jest.spyOn(ckEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ckEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ckEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
