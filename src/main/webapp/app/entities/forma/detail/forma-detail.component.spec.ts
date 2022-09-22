import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormaDetailComponent } from './forma-detail.component';

describe('Forma Management Detail Component', () => {
  let comp: FormaDetailComponent;
  let fixture: ComponentFixture<FormaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ forma: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FormaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FormaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load forma on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.forma).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
