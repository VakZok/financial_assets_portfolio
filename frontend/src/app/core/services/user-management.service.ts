import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../models/account.model";
import {AuthCoreService} from "../authentication/auth-core.service";

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  private apiUrl = 'http://localhost:8080/v1/userManagement';

  getHeader() {
    return new HttpHeaders({
      'Authorization': 'Basic ' + this.authService.getToken(),
      'Content-Type': 'application/json'
    })
  }
  constructor(private http: HttpClient, private authService:AuthCoreService) { }

  checkPItemExists(username:string): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.head<AccountModel>(this.apiUrl + '/' + username, {headers})
  }

  postAccount(account:AccountModel): Observable<AccountModel> {
    const headers = this.getHeader();
    return this.http.post<AccountModel>(this.apiUrl + '/add', account,{headers} )
  }
}
