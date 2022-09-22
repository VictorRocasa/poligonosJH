import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../forma.test-samples';

import { FormaFormService } from './forma-form.service';

describe('Forma Form Service', () => {
  let service: FormaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormaFormService);
  });

  describe('Service methods', () => {
    describe('createFormaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agrupamento: expect.any(Object),
          })
        );
      });

      it('passing IForma should create a new form with FormGroup', () => {
        const formGroup = service.createFormaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agrupamento: expect.any(Object),
          })
        );
      });
    });

    describe('getForma', () => {
      it('should return NewForma for default Forma initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFormaFormGroup(sampleWithNewData);

        const forma = service.getForma(formGroup) as any;

        expect(forma).toMatchObject(sampleWithNewData);
      });

      it('should return NewForma for empty Forma initial value', () => {
        const formGroup = service.createFormaFormGroup();

        const forma = service.getForma(formGroup) as any;

        expect(forma).toMatchObject({});
      });

      it('should return IForma', () => {
        const formGroup = service.createFormaFormGroup(sampleWithRequiredData);

        const forma = service.getForma(formGroup) as any;

        expect(forma).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IForma should not enable id FormControl', () => {
        const formGroup = service.createFormaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewForma should disable id FormControl', () => {
        const formGroup = service.createFormaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
