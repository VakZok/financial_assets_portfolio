import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../models/portfolio-item.model";
import {AggPortfolioItemModel} from "../models/agg-portfolio-item.model";

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems';

  constructor(private http: HttpClient) { }

  getPItem(id:string): Observable<PortfolioItemModel> {
    return this.http.get<PortfolioItemModel>(this.apiUrl+"/"+id);
  }

  postPItem(portfolioItem: PortfolioItemModel): Observable<PortfolioItemModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + '/add', portfolioItem)
  }

  getPItemPrevList(): Observable<PortfolioItemModel[]> {
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/preview');
  }

  getAggPItemPreview(): Observable<AggPortfolioItemModel[]> {
    return this.http.get<AggPortfolioItemModel[]>(this.apiUrl + '/viewBy/wkn/preview')
  }

  getWKNAggPItem(wkn:string): Observable<AggPortfolioItemModel> {
    return this.http.get<AggPortfolioItemModel>(this.apiUrl + '/viewBy/wkn/' + wkn)
  }

}
