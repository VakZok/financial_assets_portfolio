import {AbstractControl, ValidatorFn, ValidationErrors, AsyncValidator, AsyncValidatorFn} from '@angular/forms';
import {ShareService} from "../services/share.service";

export function WKNValidator(shareService: ShareService): AsyncValidatorFn {
  return async (control: AbstractControl): Promise<ValidationErrors | null> => {
    console.log('WKNValidator called with value:', control.value);
    try {
      const data = await shareService.getShare(control.value).toPromise();
      return { 'shareExist': true };
    } catch (error) {
      return null;
    }
  };
}
