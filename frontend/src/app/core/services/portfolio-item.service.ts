import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../models/portfolio-item.model";
import {AuthCoreService} from "../authentication/auth-core.service";

@Injectable({
  providedIn: 'root'
})
export class PortfolioItemService {

  private apiUrl = 'http://localhost:8080/v1/portfolioItems';
  private swaggerUrl = 'http://localhost:8080/v1/swagger/pItem';

  constructor(private http: HttpClient, private authService: AuthCoreService) { }

  getHeader() {
    return new HttpHeaders({
      'Authorization': 'Basic ' + this.authService.getToken(),
      'Content-Type': 'application/json'
    })
  }


  getPItemPreview(includePrice:boolean): Observable<PortfolioItemModel[]> {
    const headers = this.getHeader();
    const params = new HttpParams().set('includePrice', includePrice)
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/preview',{headers, params})
  }

  getPItemByISIN(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.get<PortfolioItemModel>(this.apiUrl + '/' + isin,{headers})
  }

  checkPItemExists(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.head<PortfolioItemModel>(this.apiUrl + '/' + isin,{headers})
  }

  postPItem(pItem:PortfolioItemModel): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.post<PortfolioItemModel>(this.apiUrl + '/add', pItem,{headers})
  }

  getPItemSwagger(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.get<PortfolioItemModel>(this.swaggerUrl + '/' + isin, {headers})
  }

  getLikedPItems(includePrice:boolean): Observable<PortfolioItemModel[]> {
    const headers = this.getHeader();
    const params = new HttpParams().set('includePrice', includePrice)
    return this.http.get<PortfolioItemModel[]>(this.apiUrl + '/liked',{headers, params})
  }

  postLike(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.post<PortfolioItemModel>(this.apiUrl + '/' + isin + '/likes', '',{headers})
  }

  deleteLike(isin:string): Observable<PortfolioItemModel> {
    const headers = this.getHeader();
    return this.http.delete<PortfolioItemModel>(this.apiUrl + '/' + isin + '/likes', {headers})
  }
}

