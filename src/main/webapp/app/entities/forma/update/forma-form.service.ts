import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IForma, NewForma } from '../forma.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IForma for edit and NewFormaFormGroupInput for create.
 */
type FormaFormGroupInput = IForma | PartialWithRequiredKeyOf<NewForma>;

type FormaFormDefaults = Pick<NewForma, 'id'>;

type FormaFormGroupContent = {
  id: FormControl<IForma['id'] | NewForma['id']>;
  agrupamento: FormControl<IForma['agrupamento']>;
};

export type FormaFormGroup = FormGroup<FormaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormaFormService {
  createFormaFormGroup(forma: FormaFormGroupInput = { id: null }): FormaFormGroup {
    const formaRawValue = {
      ...this.getFormDefaults(),
      ...forma,
    };
    return new FormGroup<FormaFormGroupContent>({
      id: new FormControl(
        { value: formaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      agrupamento: new FormControl(formaRawValue.agrupamento),
    });
  }

  getForma(form: FormaFormGroup): IForma | NewForma {
    return form.getRawValue() as IForma | NewForma;
  }

  resetForm(form: FormaFormGroup, forma: FormaFormGroupInput): void {
    const formaRawValue = { ...this.getFormDefaults(), ...forma };
    form.reset(
      {
        ...formaRawValue,
        id: { value: formaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FormaFormDefaults {
    return {
      id: null,
    };
  }
}
