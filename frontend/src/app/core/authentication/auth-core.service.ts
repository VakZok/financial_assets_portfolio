import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map } from 'rxjs';

const LOGIN_URL = 'http://localhost:8080/v1/logins';
const AUTH_TOKEN_NAME = 'authToken';

@Injectable({
  providedIn: 'root',
})
export class AuthCoreService {
  private isAuthenticatedSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private router: Router) {}

  public isAuthenticated$(): Observable<boolean> {
    let token = this.getToken();
    if (token === undefined || token === null) {
      this.setAuthState(false);
      this.router.navigate(['/login']);
      return this.isAuthenticatedSubject.asObservable();
    }
    this.setAuthState(true);
    return this.isAuthenticatedSubject.asObservable();
  }

  public login(username: string, password: string) {
    console.log("test")
    return this.http
      .get(LOGIN_URL, {
        headers: {
          authorization: this.createBasicAuthToken(username, password),
        },
      })
      .pipe(
        map((res) => {
          console.log("check")
          this.registerSuccessfulLogin(
            `${window.btoa(username + ':' + password)}`
          );
          this.setAuthState(true);
        })
      );
  }

  public logout() {
    sessionStorage.removeItem(AUTH_TOKEN_NAME);
    this.setAuthState(false);
    this.router.navigate(['/login']);
  }

  public getToken() {
    return sessionStorage.getItem(AUTH_TOKEN_NAME);
  }

  private registerSuccessfulLogin(token: string) {
    sessionStorage.setItem(AUTH_TOKEN_NAME, token);
    this.setAuthState(true);
  }

  private setAuthState(isAuthenticated: boolean): void {
    this.isAuthenticatedSubject.next(isAuthenticated);
  }

  public getAuthState(){
    return this.isAuthenticatedSubject.value
  }


  private createBasicAuthToken(username: String, password: String) {
    return 'Basic ' + window.btoa(username + ':' + password);
  }
}
