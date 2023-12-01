import {AbstractControl, FormGroup, ValidationErrors} from "@angular/forms";
import {ValidatorFn} from "@angular/forms";

export function passwordMatchValidator(accountForm: FormGroup): ValidatorFn {
    return (control:AbstractControl) : ValidationErrors | null => {
        const password = accountForm.controls['password'];
        const confirmPassword = accountForm.controls['confirmPassword'];
        const passwordsMatch = password === confirmPassword;

        return password ? null : {'mismatch': true}
    }
}
