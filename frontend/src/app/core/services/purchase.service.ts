import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseModel} from "../models/purchase.model";


@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems/';

  constructor(private http: HttpClient) { }

  postPurchase(wkn:string, purchase: PurchaseModel): Observable<PurchaseModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + wkn + '/purchases/add', purchase)
  }

  postNewPItem(purchase: PurchaseModel): Observable<PurchaseModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + 'add', purchase)
  }

  getPurchasePrevList(): Observable<PurchaseModel[]> {
    return this.http.get<PurchaseModel[]>(this.apiUrl + 'preview');
  }

}
