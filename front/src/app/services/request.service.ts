import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Request} from "../models/Request";
import {environment} from "../../env";
import {Certificate} from "../models/certificate";

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  constructor(private httpClient: HttpClient) {

  }

  getAllRequests():Observable<Request[]>{
    return this.httpClient.get<Request[]>(environment.api + 'request/all')
  }

  approveRequest(requestId: number):Observable<Certificate>{
    return this.httpClient.get<Certificate>(environment.api + 'request/accept',{
      params: new HttpParams().set('requestId', requestId)
    } )
  }


}
