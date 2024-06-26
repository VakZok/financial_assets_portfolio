import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable, of} from 'rxjs';
import { AuthCoreService } from './auth-core.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate {
    /*
    The AuthGuardService is responsible for protecting routes. It uses the AuthCoreService to check if the user is
    logged in and if the user has the required role/rights to access the desired route.
     */
    constructor(private authService: AuthCoreService, private router: Router) { }

    // Checks if the user has the required role/rights to access the desired route.
    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        let url: string = state.url;
        return this.checkUserLogin(next, url);
    }

    checkUserLogin(route: ActivatedRouteSnapshot, url: any): boolean {
      if (this.authService.getToken() !== '' && !route.data['roles'].includes(this.authService.getRole())){
          this.router.navigate(['/meinPortfolio'])
      } else if (this.authService.getToken() !== '') {
          const userRole = this.authService.getRole();
          return route.data['roles'].includes(userRole);
      } else
          this.router.navigate(['/login']);
          return false;
    }
}
