import {Component, Input, OnInit} from '@angular/core';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {Certificate} from "../models/certificate";
import {BadgeModule} from "primeng/badge";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-certificate',
  standalone: true,
  imports: [
    CardModule,
    ButtonModule,
    BadgeModule,
    DatePipe
  ],
  templateUrl: './certificate.component.html',
  styleUrl: './certificate.component.css'
})
export class CertificateComponent {
  certificate: Certificate | null = null;

  constructor() {
  }

  certificateType: string = "";
  private _certificateAlias: string | undefined = undefined;
  @Input() set certificateAlias(value: string|undefined) {
    this._certificateAlias = value;
    if (value != undefined)
      this.fetchCertificate(value);
  }

  fetchCertificate(alias: string) {
    this.certificate = {
      serialNumber: "123",
      issuerName: "Certifikey Co",
      subjectName: "Booking",
      subjectEmail: "booking@example.com",
      validFrom: new Date(Date.now()),
      validTo: new Date(Date.now() + 1230000000),
      type: "END ENTITY"
    }
  }


}
