import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountModel} from "../models/account.model";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private apiUrl = 'http://localhost:8080/v1/login';

  constructor(private http: HttpClient) {
  }

  checkUsernameExists(username: string): Observable<AccountModel> {
    return this.http.head<AccountModel>(this.apiUrl + '/' + username)
  }

  postCredentials(user: AccountModel): Observable<AccountModel> {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.post<AccountModel>(this.apiUrl + '/post', user, {headers: headers})
  }
}
