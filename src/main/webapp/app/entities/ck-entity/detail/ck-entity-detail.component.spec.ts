import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CkEntityDetailComponent } from './ck-entity-detail.component';

describe('CkEntity Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CkEntityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CkEntityDetailComponent,
              resolve: { ckEntity: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CkEntityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ckEntity on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CkEntityDetailComponent);

      // THEN
      expect(instance.ckEntity).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
