import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StackTraceElementJoularEntityDetailComponent } from './stack-trace-element-joular-entity-detail.component';

describe('StackTraceElementJoularEntity Management Detail Component', () => {
  let comp: StackTraceElementJoularEntityDetailComponent;
  let fixture: ComponentFixture<StackTraceElementJoularEntityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StackTraceElementJoularEntityDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./stack-trace-element-joular-entity-detail.component').then(m => m.StackTraceElementJoularEntityDetailComponent),
              resolve: { stackTraceElementJoularEntity: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StackTraceElementJoularEntityDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StackTraceElementJoularEntityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stackTraceElementJoularEntity on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StackTraceElementJoularEntityDetailComponent);

      // THEN
      expect(instance.stackTraceElementJoularEntity()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
