import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../env";
import {CertificateNode} from "../models/CertificateNode";
import {Observable} from "rxjs";
import {Certificate} from "../models/certificate";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) {

  }
  getByAlias(alias: string){
    return this.httpClient.get(environment.api + "alias/" + alias );
  }

  getBySerialNumber(number: string): Observable<Certificate>{
    return this.httpClient.get<Certificate>(environment.api + "certificate/" + number);
  }

  getAllCertificateNodes(): Observable<CertificateNode[]>{
    return this.httpClient.get<CertificateNode[]>(environment.api + "certificate/all")
  }
}
