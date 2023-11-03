import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../models/portfolio-item.model";
import {ShareModel} from "../models/share.model";

@Injectable({
  providedIn: 'root'
})
export class ShareService {

  private apiUrl = 'http://localhost:8080/v1/shares';

  constructor(private http: HttpClient) { }

  getShare(wkn:string): Observable<PortfolioItemModel> {
    return this.http.get<PortfolioItemModel>(this.apiUrl+"/"+wkn);
  }

  postShare(share:ShareModel): Observable<ShareModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + '/add', share, { headers: headers })
  }
}
