import {AbstractControl, ValidationErrors} from "@angular/forms";
import {ValidatorFn} from "@angular/forms";

export function passwordMatchValidator(password: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const validPassword = control.value === password;
        return validPassword ? { password: { value: control.value } } : null;
    };
}
