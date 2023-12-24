import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../models/portfolio-item.model";
import {AuthCoreService} from "../authentication/auth-core.service";

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems';
  private swaggerUrl = 'http://localhost:8080/swagger/pItem/{isin}';

  constructor(private http: HttpClient, private authService: AuthCoreService) { }

  getHeader() {
    return new HttpHeaders({
      'Authorization': 'Basic ' + this.authService.getToken(),
      'Content-Type': 'application/json'
    })
  }

  getPItemPreview(): Observable<PortfolioItemModel[]> {
    const headers = this.getHeader();
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/preview',{headers})
  }

  getPItemByWKN(wkn:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.get<PortfolioItemModel>(this.apiUrl + '/' + wkn,{headers})
  }

  checkPItemExists(wkn:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.head<PortfolioItemModel>(this.apiUrl + '/' + wkn,{headers})
  }

  postPItem(pItem:PortfolioItemModel): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.post<PortfolioItemModel>(this.apiUrl + '/add', pItem,{headers})
  }

  getPItemSwagger(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.get<PortfolioItemModel>(this.swaggerUrl + '/' + isin, {headers})
  }
}
