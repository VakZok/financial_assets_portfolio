import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map } from 'rxjs';
import {LoginModel} from "../models/login.model";

const LOGIN_URL = 'http://localhost:8080/v1/logins';
const AUTH_TOKEN_NAME = 'authToken';

@Injectable({
  providedIn: 'root',
})
export class AuthCoreService {
  private isAuthenticatedSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  private role : string = '';
  private name : string = '';

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
          console.log("check",res);

          let lm : LoginModel = <LoginModel>res;
          console.log("test", `${window.btoa(username + ':' + password)}`)
          this.registerSuccessfulLogin(
            `${window.btoa(username + ':' + password)}`,
            lm.name, lm.role
          );

          this.setAuthState(true);
        })
      );
  }

  public logout() {
    sessionStorage.removeItem(AUTH_TOKEN_NAME);
    this.role = '';
    this.name = '';
    this.setAuthState(false);
    this.router.navigate(['/login']);
  }

  public getToken() {
    let token = ''
    if (sessionStorage.getItem(AUTH_TOKEN_NAME) != null) {
      token = sessionStorage.getItem(AUTH_TOKEN_NAME) || '';
    }
    console.log(token)
    return token
  }

  private registerSuccessfulLogin(token: string, name : string, role : string) {
    sessionStorage.setItem(AUTH_TOKEN_NAME, token);
    this.role = role;
    this.name = name;
    this.setAuthState(true);
  }

  private setAuthState(isAuthenticated: boolean): void {
    this.isAuthenticatedSubject.next(isAuthenticated);
  }

  public getAuthState(){
    return this.isAuthenticatedSubject.value
  }

  public getRole(){
    return this.role;
  }

  public getName() {
    return this.name;
  }


  private createBasicAuthToken(username: String, password: String) {
    return 'Basic ' + window.btoa(username + ':' + password);
  }
}
