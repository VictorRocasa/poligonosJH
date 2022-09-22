import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormaFormService } from './forma-form.service';
import { FormaService } from '../service/forma.service';
import { IForma } from '../forma.model';

import { FormaUpdateComponent } from './forma-update.component';

describe('Forma Management Update Component', () => {
  let comp: FormaUpdateComponent;
  let fixture: ComponentFixture<FormaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formaFormService: FormaFormService;
  let formaService: FormaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormaUpdateComponent],
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
      .overrideTemplate(FormaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formaFormService = TestBed.inject(FormaFormService);
    formaService = TestBed.inject(FormaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Forma query and add missing value', () => {
      const forma: IForma = { id: 456 };
      const agrupamento: IForma = { id: 1965 };
      forma.agrupamento = agrupamento;

      const formaCollection: IForma[] = [{ id: 26463 }];
      jest.spyOn(formaService, 'query').mockReturnValue(of(new HttpResponse({ body: formaCollection })));
      const additionalFormas = [agrupamento];
      const expectedCollection: IForma[] = [...additionalFormas, ...formaCollection];
      jest.spyOn(formaService, 'addFormaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ forma });
      comp.ngOnInit();

      expect(formaService.query).toHaveBeenCalled();
      expect(formaService.addFormaToCollectionIfMissing).toHaveBeenCalledWith(
        formaCollection,
        ...additionalFormas.map(expect.objectContaining)
      );
      expect(comp.formasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const forma: IForma = { id: 456 };
      const agrupamento: IForma = { id: 9006 };
      forma.agrupamento = agrupamento;

      activatedRoute.data = of({ forma });
      comp.ngOnInit();

      expect(comp.formasSharedCollection).toContain(agrupamento);
      expect(comp.forma).toEqual(forma);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForma>>();
      const forma = { id: 123 };
      jest.spyOn(formaFormService, 'getForma').mockReturnValue(forma);
      jest.spyOn(formaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forma }));
      saveSubject.complete();

      // THEN
      expect(formaFormService.getForma).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formaService.update).toHaveBeenCalledWith(expect.objectContaining(forma));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForma>>();
      const forma = { id: 123 };
      jest.spyOn(formaFormService, 'getForma').mockReturnValue({ id: null });
      jest.spyOn(formaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forma: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forma }));
      saveSubject.complete();

      // THEN
      expect(formaFormService.getForma).toHaveBeenCalled();
      expect(formaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IForma>>();
      const forma = { id: 123 };
      jest.spyOn(formaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forma });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formaService.update).toHaveBeenCalled();
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
