import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../env";
import {CertificateNode} from "../models/CertificateNode";
import {Observable} from "rxjs";
import {Certificate} from "../models/certificate";
import {Request} from "../models/Request";

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


  generateNewCertificate(request: Request):Observable<Certificate>{
    return this.httpClient.post<Certificate>(environment.api + "generate/get",request );
  }

  deleteCertificate(serialNumber: string){
    return this.httpClient.delete(environment.api + "certificate/" + serialNumber);
  }
}
