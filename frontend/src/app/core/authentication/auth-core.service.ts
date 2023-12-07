import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map } from 'rxjs';
import {LoginModel} from "../models/login.model";
import {MatSnackBar} from "@angular/material/snack-bar";

const LOGIN_URL = 'http://localhost:8080/v1/logins';
const AUTH_TOKEN_NAME = 'authToken';

@Injectable({
  providedIn: 'root',
})
export class AuthCoreService {
  private isAuthenticatedSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  private username : string = '';
  private role : string = '';
  private name : string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private snackBar: MatSnackBar) {}

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
    return this.http
      .get(LOGIN_URL, {
        headers: {
          authorization: this.createBasicAuthToken(username, password),
        },
      })
      .pipe(
        map((res) => {
          let lm : LoginModel = <LoginModel> res;
          this.registerSuccessfulLogin(
            `${window.btoa(username + ':' + password)}`,
            username, lm.name, lm.role
          );
          this.setAuthState(true);
          this.snackBar.open('Erfolgreich eingeloggt ✔️', '', {
            duration: 3000
          });
        })
      );
  }

  public logout() {
    sessionStorage.removeItem(AUTH_TOKEN_NAME);
    this.role = '';
    this.name = '';
    this.username = '';
    this.setAuthState(false);
    this.router.navigate(['/login']);
  }

  public getToken() {
    let token = ''
    if (sessionStorage.getItem(AUTH_TOKEN_NAME) != null) {
      token = sessionStorage.getItem(AUTH_TOKEN_NAME) || '';
    }
    return token
  }

  private registerSuccessfulLogin(token: string, username: string,  name: string, role: string) {
    sessionStorage.setItem(AUTH_TOKEN_NAME, token);
    this.role = role;
    this.name = name;
    this.username = username;
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
  public getUsername() {
    return this.username;
  }

  private createBasicAuthToken(username: String, password: String) {
    return 'Basic ' + window.btoa(username + ':' + password);
  }
}
