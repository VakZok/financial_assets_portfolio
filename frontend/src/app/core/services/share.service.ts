import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseModel} from "../models/purchase.model";
import {ShareModel} from "../models/share.model";

@Injectable({
  providedIn: 'root'
})
export class ShareService {

  private apiUrl = 'http://localhost:8080/v1/shares';

  constructor(private http: HttpClient) { }

  getShare(wkn:string): Observable<ShareModel> {
    return this.http.get<ShareModel>(this.apiUrl+"/"+wkn);
  }

  checkShareExists(wkn:string): Observable<ShareModel> {
    return this.http.head<ShareModel>(this.apiUrl+"/"+wkn);
  }

  postShare(share:ShareModel): Observable<ShareModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + '/add', share, { headers: headers })
  }
}
