import { Injectable } from '@angular/core';
import {CanActivate, Router, UrlTree} from '@angular/router';
import {map, Observable, of} from 'rxjs';
import { AuthCoreService } from './auth-core.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate {
  constructor(private auth: AuthCoreService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.auth.isAuthenticated$().pipe(
      map(isAuthenticated => {
        if (isAuthenticated) {
          return true;
        } else {
          return this.router.createUrlTree(['/login']);
        }
      })
    );
  }
}
