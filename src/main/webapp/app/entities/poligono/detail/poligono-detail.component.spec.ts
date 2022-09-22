import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PoligonoDetailComponent } from './poligono-detail.component';

describe('Poligono Management Detail Component', () => {
  let comp: PoligonoDetailComponent;
  let fixture: ComponentFixture<PoligonoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PoligonoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ poligono: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PoligonoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PoligonoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load poligono on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.poligono).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
