import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../env";
import {CertificateNode} from "../models/CertificateNode";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) {

  }
  getByAlias(alias: string){
    return this.httpClient.get(environment.api + "alias/" + alias );
  }

  getAllCertificateNodes(): Observable<CertificateNode[]>{
    return this.httpClient.get<CertificateNode[]>(environment.api + "certificate/all")
  }
}
