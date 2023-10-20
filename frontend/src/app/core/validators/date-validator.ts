import { AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';

export function DateValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const maxDate = new Date("2123-12-31");
        const minDate: Date = new Date("1903-04-22");
        const inputDate = new Date(control.value);

        if (!(inputDate < maxDate && inputDate > minDate)) {
            return { 'dateInvalid': true };
        }
        return null;
    };
}
