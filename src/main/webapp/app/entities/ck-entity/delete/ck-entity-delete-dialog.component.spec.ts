jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CkEntityService } from '../service/ck-entity.service';

import { CkEntityDeleteDialogComponent } from './ck-entity-delete-dialog.component';

describe('CkEntity Management Delete Component', () => {
  let comp: CkEntityDeleteDialogComponent;
  let fixture: ComponentFixture<CkEntityDeleteDialogComponent>;
  let service: CkEntityService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CkEntityDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CkEntityDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CkEntityDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CkEntityService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
