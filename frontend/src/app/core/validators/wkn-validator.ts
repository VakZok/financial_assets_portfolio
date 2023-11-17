import {AbstractControl, ValidatorFn, ValidationErrors, AsyncValidator, AsyncValidatorFn} from '@angular/forms';

import {catchError, debounceTime, map, Observable, of, switchMap, take} from "rxjs";
import {PortfolioItemService} from "../services/portfolio-item.service";


export function WKNValidator(pItemService: PortfolioItemService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return control.valueChanges.pipe(
      debounceTime(100), // Delay the validation for 100 milliseconds to avoid spamming the server
      switchMap(value => {
        return pItemService.checkPItemExists(value).pipe(
          map(data => ({ 'pItemExists': true })),
          catchError(() => of(null))
        );
      }),
      take(1) // Ensure the observable completes after emitting a single value
    );
  };
}
