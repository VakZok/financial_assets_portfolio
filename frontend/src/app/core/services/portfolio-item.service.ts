import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseModel} from "../models/purchase.model";
import {PortfolioItemModel} from "../models/portfolio-item.model";

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems';

  constructor(private http: HttpClient) { }


  getAggPItemPreview(): Observable<PortfolioItemModel[]> {
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/viewBy/wkn/preview')
  }

  getWKNAggPItem(wkn:string): Observable<PortfolioItemModel> {
    return this.http.get<PortfolioItemModel>(this.apiUrl + '/viewBy/wkn/' + wkn)
  }

}
