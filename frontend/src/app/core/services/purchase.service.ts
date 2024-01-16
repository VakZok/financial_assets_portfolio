import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseModel} from "../models/purchase.model";
import {AuthCoreService} from "../authentication/auth-core.service";


@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  /*
  This Service is responsible for all communication with the backend API regarding purchases.
  It is injected with the HttpClient and the AuthCoreService to be able to send requests to the backend API.
   */

  private apiUrl = 'http://localhost:8080/v1/portfolioItems/';

  constructor(private http: HttpClient, private authService: AuthCoreService) { }

  getHeader() {
    return new HttpHeaders({
      'Authorization': 'Basic ' + this.authService.getToken(),
      'Content-Type': 'application/json'
    })
  }

  postPurchase(wkn:string, purchase: PurchaseModel): Observable<PurchaseModel>{
    const headers = this.getHeader();
    return this.http.post<any>(this.apiUrl + wkn + '/purchases/add', purchase,{headers})
  }

  postNewPItem(purchase: PurchaseModel): Observable<PurchaseModel>{
    const headers = this.getHeader();
    return this.http.post<any>(this.apiUrl + 'add', purchase,{headers})
  }

  getPurchasePrevList(): Observable<PurchaseModel[]> {
    const headers = this.getHeader();
    return this.http.get<PurchaseModel[]>(this.apiUrl + 'preview',{headers});
  }

}
