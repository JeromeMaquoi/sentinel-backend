import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IConstructorEntity } from 'app/entities/constructor-entity/constructor-entity.model';
import { ConstructorEntityService } from 'app/entities/constructor-entity/service/constructor-entity.service';
import { AttributeEntityService } from '../service/attribute-entity.service';
import { IAttributeEntity } from '../attribute-entity.model';
import { AttributeEntityFormService } from './attribute-entity-form.service';

import { AttributeEntityUpdateComponent } from './attribute-entity-update.component';

describe('AttributeEntity Management Update Component', () => {
  let comp: AttributeEntityUpdateComponent;
  let fixture: ComponentFixture<AttributeEntityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attributeEntityFormService: AttributeEntityFormService;
  let attributeEntityService: AttributeEntityService;
  let constructorEntityService: ConstructorEntityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AttributeEntityUpdateComponent],
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
      .overrideTemplate(AttributeEntityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttributeEntityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attributeEntityFormService = TestBed.inject(AttributeEntityFormService);
    attributeEntityService = TestBed.inject(AttributeEntityService);
    constructorEntityService = TestBed.inject(ConstructorEntityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ConstructorEntity query and add missing value', () => {
      const attributeEntity: IAttributeEntity = { id: 'CBA' };
      const constructorEntity: IConstructorEntity = { id: '92056955-f9e8-4778-ab26-38e05fa6d446' };
      attributeEntity.constructorEntity = constructorEntity;

      const constructorEntityCollection: IConstructorEntity[] = [{ id: '5b504d80-88f9-41fb-9c87-5ac8e93c01a0' }];
      jest.spyOn(constructorEntityService, 'query').mockReturnValue(of(new HttpResponse({ body: constructorEntityCollection })));
      const additionalConstructorEntities = [constructorEntity];
      const expectedCollection: IConstructorEntity[] = [...additionalConstructorEntities, ...constructorEntityCollection];
      jest.spyOn(constructorEntityService, 'addConstructorEntityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attributeEntity });
      comp.ngOnInit();

      expect(constructorEntityService.query).toHaveBeenCalled();
      expect(constructorEntityService.addConstructorEntityToCollectionIfMissing).toHaveBeenCalledWith(
        constructorEntityCollection,
        ...additionalConstructorEntities.map(expect.objectContaining),
      );
      expect(comp.constructorEntitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const attributeEntity: IAttributeEntity = { id: 'CBA' };
      const constructorEntity: IConstructorEntity = { id: '6d619cc0-f3d1-4963-9c96-38aa602700f1' };
      attributeEntity.constructorEntity = constructorEntity;

      activatedRoute.data = of({ attributeEntity });
      comp.ngOnInit();

      expect(comp.constructorEntitiesSharedCollection).toContain(constructorEntity);
      expect(comp.attributeEntity).toEqual(attributeEntity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttributeEntity>>();
      const attributeEntity = { id: 'ABC' };
      jest.spyOn(attributeEntityFormService, 'getAttributeEntity').mockReturnValue(attributeEntity);
      jest.spyOn(attributeEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attributeEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attributeEntity }));
      saveSubject.complete();

      // THEN
      expect(attributeEntityFormService.getAttributeEntity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attributeEntityService.update).toHaveBeenCalledWith(expect.objectContaining(attributeEntity));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttributeEntity>>();
      const attributeEntity = { id: 'ABC' };
      jest.spyOn(attributeEntityFormService, 'getAttributeEntity').mockReturnValue({ id: null });
      jest.spyOn(attributeEntityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attributeEntity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attributeEntity }));
      saveSubject.complete();

      // THEN
      expect(attributeEntityFormService.getAttributeEntity).toHaveBeenCalled();
      expect(attributeEntityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttributeEntity>>();
      const attributeEntity = { id: 'ABC' };
      jest.spyOn(attributeEntityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attributeEntity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attributeEntityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConstructorEntity', () => {
      it('Should forward to constructorEntityService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(constructorEntityService, 'compareConstructorEntity');
        comp.compareConstructorEntity(entity, entity2);
        expect(constructorEntityService.compareConstructorEntity).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
