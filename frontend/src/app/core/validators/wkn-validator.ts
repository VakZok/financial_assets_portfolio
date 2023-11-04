import { AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';
import {ShareService} from "../services/share.service";


export function validateWKN(shareService : ShareService): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const wkn = control.value
    shareService.getShare(wkn).subscribe({
      // if backend validation produces exceptions on postPItem, set them on the errorMap
      error: (errors) => {
        return {'wknExists': true};
      }
    })
    return null
  }
}
