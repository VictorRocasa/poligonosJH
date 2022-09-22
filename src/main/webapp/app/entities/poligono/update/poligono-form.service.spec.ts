import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../poligono.test-samples';

import { PoligonoFormService } from './poligono-form.service';

describe('Poligono Form Service', () => {
  let service: PoligonoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PoligonoFormService);
  });

  describe('Service methods', () => {
    describe('createPoligonoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPoligonoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lados: expect.any(Object),
            tamanho: expect.any(Object),
            dataCriacao: expect.any(Object),
            ultimaModificacao: expect.any(Object),
            forma: expect.any(Object),
          })
        );
      });

      it('passing IPoligono should create a new form with FormGroup', () => {
        const formGroup = service.createPoligonoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            lados: expect.any(Object),
            tamanho: expect.any(Object),
            dataCriacao: expect.any(Object),
            ultimaModificacao: expect.any(Object),
            forma: expect.any(Object),
          })
        );
      });
    });

    describe('getPoligono', () => {
      it('should return NewPoligono for default Poligono initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPoligonoFormGroup(sampleWithNewData);

        const poligono = service.getPoligono(formGroup) as any;

        expect(poligono).toMatchObject(sampleWithNewData);
      });

      it('should return NewPoligono for empty Poligono initial value', () => {
        const formGroup = service.createPoligonoFormGroup();

        const poligono = service.getPoligono(formGroup) as any;

        expect(poligono).toMatchObject({});
      });

      it('should return IPoligono', () => {
        const formGroup = service.createPoligonoFormGroup(sampleWithRequiredData);

        const poligono = service.getPoligono(formGroup) as any;

        expect(poligono).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPoligono should not enable id FormControl', () => {
        const formGroup = service.createPoligonoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPoligono should disable id FormControl', () => {
        const formGroup = service.createPoligonoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
