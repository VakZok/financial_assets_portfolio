import {AbstractControl, ValidatorFn, ValidationErrors, AsyncValidator, AsyncValidatorFn} from '@angular/forms';
import {ShareService} from "../services/share.service";
import {catchError, debounceTime, map, Observable, of, switchMap, take} from "rxjs";


export function WKNValidator(shareService: ShareService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return control.valueChanges.pipe(
      debounceTime(100), // Delay the validation for 300 milliseconds to avoid spamming the server
      switchMap(value => {
        return shareService.checkShareExists(value).pipe(
          map(data => ({ 'shareExist': true })),
          catchError(() => of(null))
        );
      }),
      take(1) // Ensure the observable completes after emitting a single value
    );
  };
}
