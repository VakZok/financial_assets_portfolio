import { AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';

// Validator to check if the input is a valid date
export function DateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const maxDate = new Date("2123-12-31");
        const minDate: Date = new Date("1903-04-22");
        const inputDate = new Date(control.value);

        if (!(inputDate < maxDate)) {
          return {'dateFutureErr': true};
        } else if (!(inputDate > minDate)){
          return{'datePastErr':true};
        }
        return null;
    };
}
