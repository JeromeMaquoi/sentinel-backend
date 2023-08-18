import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CommitEntityDetailComponent } from './commit-entity-detail.component';

describe('CommitEntity Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommitEntityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CommitEntityDetailComponent,
              resolve: { commitEntity: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CommitEntityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load commitEntity on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CommitEntityDetailComponent);

      // THEN
      expect(instance.commitEntity).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
