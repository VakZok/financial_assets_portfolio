import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
}
