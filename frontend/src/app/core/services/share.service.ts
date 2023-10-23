import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {ShareModel} from "../models/share.model";


@Injectable({
  providedIn: 'root'
})
export class ShareService {

  private apiUrl = 'http://localhost:8080/v1/shares';

  constructor(private http: HttpClient) { }

  getShares(): Observable<ShareModel[]> {
    return this.http.get<ShareModel[]>(this.apiUrl);
  }

  postShare(share: ShareModel): Observable<ShareModel>{
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl + '/add', share, { headers: headers })
  }

}
