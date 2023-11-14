import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../models/portfolio-item.model";

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems';

  constructor(private http: HttpClient) { }


  getPItemPreview(): Observable<PortfolioItemModel[]> {
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/preview')
  }

  getPItemByWKN(wkn:string): Observable<PortfolioItemModel> {
    return this.http.get<PortfolioItemModel>(this.apiUrl + '/' + wkn )
  }

  checkPItemExists(wkn:string): Observable<PortfolioItemModel> {
    return this.http.head<PortfolioItemModel>(this.apiUrl + '/' + wkn )
  }

  postPItem(pItem:PortfolioItemModel): Observable<PortfolioItemModel> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<PortfolioItemModel>(this.apiUrl + '/add', pItem, { headers: headers } )
  }

}
