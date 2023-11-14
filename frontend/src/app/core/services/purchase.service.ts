import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseModel} from "../models/purchase.model";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private apiUrl = 'http://localhost:8080/v1/purchases';

  constructor(private http: HttpClient) { }

  getPurchase(id:string): Observable<PurchaseModel> {
    return this.http.get<PurchaseModel>(this.apiUrl+"/"+id);
  }

  postPurchase(purchase: PurchaseModel): Observable<PurchaseModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + '/add', purchase)
  }

  getPurchasePrevList(): Observable<PurchaseModel[]> {
    return this.http.get<PurchaseModel[]>(this.apiUrl + '/preview');
  }

}
