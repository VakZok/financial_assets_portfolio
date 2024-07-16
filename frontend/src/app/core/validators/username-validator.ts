import {AbstractControl, ValidationErrors, AsyncValidatorFn} from '@angular/forms';
import {catchError, debounceTime, map, Observable, of, switchMap, take} from "rxjs";
import {UserManagementService} from "../services/user-management.service";

// Validator to check if the username exists according to the Database
export function UsernameValidator(userManService: UserManagementService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
        return control.valueChanges.pipe(
            debounceTime(100), // Delay the validation for 100 milliseconds to avoid spamming the server
            switchMap(value => {
                return userManService.checkUsernameExists(value).pipe(
                    map(data => ({ 'usernameExists': true })),
                    catchError(() => of(null))
                );
            }),
            take(1) // Ensure the observable completes after emitting a single value
        );
    };
}
