import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../models/account.model";
import {AuthCoreService} from "../authentication/auth-core.service";

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  /*
  This service is responsible for all communication with the backend API regarding user management.
  It is injected with the HttpClient and the AuthCoreService to be able to send requests to the backend API.
   */

  private apiUrl = 'http://localhost:8080/v1/accounts';

  getHeader() {
    return new HttpHeaders({
      'Authorization': 'Basic ' + this.authService.getToken(),
      'Content-Type': 'application/json'
    })
  }
  constructor(private http: HttpClient, private authService:AuthCoreService) { }


  checkUsernameExists(username: string): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.head<AccountModel>(this.apiUrl + '/' + username, {headers: headers})
  }

  postAccount(account:AccountModel): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.post<AccountModel>(this.apiUrl + '/' + account.username + '/add', account, {headers: headers})
  }

  getAccountPreview(): Observable<AccountModel[]> {
    const headers = this.getHeader();
    return this.http.get<AccountModel[]>(this.apiUrl + '/preview', {headers: headers})
  }

  getAccountByUsername(username:string): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.get<AccountModel>(this.apiUrl + '/' + username, {headers: headers})
  }

  putAccount(initUsername:string, account:AccountModel): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.put<AccountModel>(this.apiUrl + '/' + initUsername, account, {headers: headers})
  }

  deleteAccount(username:string): Observable<AccountModel>{
    const headers = this.getHeader();
    return this.http.delete<AccountModel>(this.apiUrl + '/' + username, {headers: headers})

  }
}
