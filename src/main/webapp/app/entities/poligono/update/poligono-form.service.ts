import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPoligono, NewPoligono } from '../poligono.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPoligono for edit and NewPoligonoFormGroupInput for create.
 */
type PoligonoFormGroupInput = IPoligono | PartialWithRequiredKeyOf<NewPoligono>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPoligono | NewPoligono> = Omit<T, 'dataCriacao' | 'ultimaModificacao'> & {
  dataCriacao?: string | null;
  ultimaModificacao?: string | null;
};

type PoligonoFormRawValue = FormValueOf<IPoligono>;

type NewPoligonoFormRawValue = FormValueOf<NewPoligono>;

type PoligonoFormDefaults = Pick<NewPoligono, 'id' | 'dataCriacao' | 'ultimaModificacao'>;

type PoligonoFormGroupContent = {
  id: FormControl<PoligonoFormRawValue['id'] | NewPoligono['id']>;
  lados: FormControl<PoligonoFormRawValue['lados']>;
  tamanho: FormControl<PoligonoFormRawValue['tamanho']>;
  dataCriacao: FormControl<PoligonoFormRawValue['dataCriacao']>;
  ultimaModificacao: FormControl<PoligonoFormRawValue['ultimaModificacao']>;
  forma: FormControl<PoligonoFormRawValue['forma']>;
};

export type PoligonoFormGroup = FormGroup<PoligonoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PoligonoFormService {
  createPoligonoFormGroup(poligono: PoligonoFormGroupInput = { id: null }): PoligonoFormGroup {
    const poligonoRawValue = this.convertPoligonoToPoligonoRawValue({
      ...this.getFormDefaults(),
      ...poligono,
    });
    return new FormGroup<PoligonoFormGroupContent>({
      id: new FormControl(
        { value: poligonoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      lados: new FormControl(poligonoRawValue.lados, {
        validators: [Validators.required],
      }),
      tamanho: new FormControl(poligonoRawValue.tamanho, {
        validators: [Validators.required],
      }),
      dataCriacao: new FormControl(poligonoRawValue.dataCriacao, {
        validators: [Validators.required],
      }),
      ultimaModificacao: new FormControl(poligonoRawValue.ultimaModificacao, {
        validators: [Validators.required],
      }),
      forma: new FormControl(poligonoRawValue.forma),
    });
  }

  getPoligono(form: PoligonoFormGroup): IPoligono | NewPoligono {
    return this.convertPoligonoRawValueToPoligono(form.getRawValue() as PoligonoFormRawValue | NewPoligonoFormRawValue);
  }

  resetForm(form: PoligonoFormGroup, poligono: PoligonoFormGroupInput): void {
    const poligonoRawValue = this.convertPoligonoToPoligonoRawValue({ ...this.getFormDefaults(), ...poligono });
    form.reset(
      {
        ...poligonoRawValue,
        id: { value: poligonoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PoligonoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataCriacao: currentTime,
      ultimaModificacao: currentTime,
    };
  }

  private convertPoligonoRawValueToPoligono(rawPoligono: PoligonoFormRawValue | NewPoligonoFormRawValue): IPoligono | NewPoligono {
    return {
      ...rawPoligono,
      dataCriacao: dayjs(rawPoligono.dataCriacao, DATE_TIME_FORMAT),
      ultimaModificacao: dayjs(rawPoligono.ultimaModificacao, DATE_TIME_FORMAT),
    };
  }

  private convertPoligonoToPoligonoRawValue(
    poligono: IPoligono | (Partial<NewPoligono> & PoligonoFormDefaults)
  ): PoligonoFormRawValue | PartialWithRequiredKeyOf<NewPoligonoFormRawValue> {
    return {
      ...poligono,
      dataCriacao: poligono.dataCriacao ? poligono.dataCriacao.format(DATE_TIME_FORMAT) : undefined,
      ultimaModificacao: poligono.ultimaModificacao ? poligono.ultimaModificacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
