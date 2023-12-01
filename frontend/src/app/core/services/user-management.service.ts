import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  private apiUrl = 'http://localhost:8080/v1/userManagement';

  constructor(private http: HttpClient) { }

  checkPItemExists(username:string): Observable<AccountModel> {
    return this.http.head<AccountModel>(this.apiUrl + '/' + username )
  }

  postAccount(account:AccountModel): Observable<AccountModel> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<AccountModel>(this.apiUrl + '/add', account, { headers: headers } )
  }
}
