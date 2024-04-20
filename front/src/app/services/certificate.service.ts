import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../env";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) {

  }
  getByAlias(alias: string){
    return this.httpClient.get(environment.api + "alias/" + alias );
  }
}
