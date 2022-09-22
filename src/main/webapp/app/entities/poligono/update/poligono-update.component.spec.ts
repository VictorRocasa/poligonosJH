import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PoligonoFormService } from './poligono-form.service';
import { PoligonoService } from '../service/poligono.service';
import { IPoligono } from '../poligono.model';
import { IForma } from 'app/entities/forma/forma.model';
import { FormaService } from 'app/entities/forma/service/forma.service';

import { PoligonoUpdateComponent } from './poligono-update.component';

describe('Poligono Management Update Component', () => {
  let comp: PoligonoUpdateComponent;
  let fixture: ComponentFixture<PoligonoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let poligonoFormService: PoligonoFormService;
  let poligonoService: PoligonoService;
  let formaService: FormaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PoligonoUpdateComponent],
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
      .overrideTemplate(PoligonoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PoligonoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    poligonoFormService = TestBed.inject(PoligonoFormService);
    poligonoService = TestBed.inject(PoligonoService);
    formaService = TestBed.inject(FormaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Forma query and add missing value', () => {
      const poligono: IPoligono = { id: 456 };
      const forma: IForma = { id: 31517 };
      poligono.forma = forma;

      const formaCollection: IForma[] = [{ id: 10371 }];
      jest.spyOn(formaService, 'query').mockReturnValue(of(new HttpResponse({ body: formaCollection })));
      const additionalFormas = [forma];
      const expectedCollection: IForma[] = [...additionalFormas, ...formaCollection];
      jest.spyOn(formaService, 'addFormaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ poligono });
      comp.ngOnInit();

      expect(formaService.query).toHaveBeenCalled();
      expect(formaService.addFormaToCollectionIfMissing).toHaveBeenCalledWith(
        formaCollection,
        ...additionalFormas.map(expect.objectContaining)
      );
      expect(comp.formasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const poligono: IPoligono = { id: 456 };
      const forma: IForma = { id: 59775 };
      poligono.forma = forma;

      activatedRoute.data = of({ poligono });
      comp.ngOnInit();

      expect(comp.formasSharedCollection).toContain(forma);
      expect(comp.poligono).toEqual(poligono);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPoligono>>();
      const poligono = { id: 123 };
      jest.spyOn(poligonoFormService, 'getPoligono').mockReturnValue(poligono);
      jest.spyOn(poligonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poligono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poligono }));
      saveSubject.complete();

      // THEN
      expect(poligonoFormService.getPoligono).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(poligonoService.update).toHaveBeenCalledWith(expect.objectContaining(poligono));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPoligono>>();
      const poligono = { id: 123 };
      jest.spyOn(poligonoFormService, 'getPoligono').mockReturnValue({ id: null });
      jest.spyOn(poligonoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poligono: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poligono }));
      saveSubject.complete();

      // THEN
      expect(poligonoFormService.getPoligono).toHaveBeenCalled();
      expect(poligonoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPoligono>>();
      const poligono = { id: 123 };
      jest.spyOn(poligonoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poligono });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(poligonoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareForma', () => {
      it('Should forward to formaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formaService, 'compareForma');
        comp.compareForma(entity, entity2);
        expect(formaService.compareForma).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
