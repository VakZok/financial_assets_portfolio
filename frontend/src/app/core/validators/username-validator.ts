import {AbstractControl, ValidatorFn, ValidationErrors, AsyncValidator, AsyncValidatorFn} from '@angular/forms';
import {catchError, debounceTime, map, Observable, of, switchMap, take} from "rxjs";
import { AuthenticationService} from "../services/authentication.service";

export function UsernameValidator(authicationService: AuthenticationService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return control.valueChanges.pipe(
      debounceTime(100), // Delay the validation for 100 milliseconds to avoid spamming the server
      switchMap(value => {
        return authicationService.checkPItemExists(value).pipe(
          map(data => ({ 'usernameExists': true })),
          catchError(() => of(null))
        );
      }),
      take(1) // Ensure the observable completes after emitting a single value
    );
  };
}
