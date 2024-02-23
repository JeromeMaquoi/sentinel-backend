import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { JoularNodeEntityDetailComponent } from './joular-node-entity-detail.component';

describe('JoularNodeEntity Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JoularNodeEntityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: JoularNodeEntityDetailComponent,
              resolve: { joularNodeEntity: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(JoularNodeEntityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load joularNodeEntity on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', JoularNodeEntityDetailComponent);

      // THEN
      expect(instance.joularNodeEntity).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
