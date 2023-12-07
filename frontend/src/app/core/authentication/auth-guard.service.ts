import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {map, Observable, of} from 'rxjs';
import { AuthCoreService } from './auth-core.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate {

    constructor(private authService: AuthCoreService, private router: Router) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        let url: string = state.url;
        return this.checkUserLogin(next, url);
    }

    checkUserLogin(route: ActivatedRouteSnapshot, url: any): boolean {
        if (this.authService.getAuthState()) {
            const userRole = this.authService.getRole();
            return route.data['roles'].includes(userRole);
        }
        this.router.navigate(['/login']);
        return false;
    }
}
