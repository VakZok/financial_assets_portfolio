import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  private apiUrl = 'http://localhost:8080/v1/accounts';

  constructor(private http: HttpClient) { }

  checkUsernameExists(username: string): Observable<AccountModel> {
    return this.http.head<AccountModel>(this.apiUrl + '/' + username)
  }

  postAccount(account:AccountModel): Observable<AccountModel> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<AccountModel>(this.apiUrl + '/' + account.username + '/add', account, { headers: headers } )
  } 

  getAccountPreview(): Observable<AccountModel[]> {
    return this.http.get<AccountModel[]>(this.apiUrl + '/preview')
  }

  getAccountByUsername(username:string): Observable<AccountModel> {
    return this.http.get<AccountModel>(this.apiUrl + '/' + username )
  }

  putAccount(account:AccountModel): Observable<AccountModel> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<AccountModel>(this.apiUrl + '/' + account.username, account, { headers: headers } )
  }

  deleteAccount(username:string): Observable<AccountModel>{
    return this.http.delete<AccountModel>(this.apiUrl + '/' + username )
  }
}
