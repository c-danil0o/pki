import {Component, Input, OnInit} from '@angular/core';
import {CardModule} from "primeng/card";
import {ButtonModule} from "primeng/button";
import {Certificate} from "../models/certificate";
import {BadgeModule} from "primeng/badge";
import {DatePipe} from "@angular/common";
import {CertificateService} from "../services/certificate.service";

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

  constructor(private certificateService: CertificateService) {
  }

  certificateType: string = "";
  private _certificateAlias: string | undefined = undefined;
  @Input() set certificateAlias(value: string|undefined) {
    this._certificateAlias = value;
    if (value != undefined)
      this.fetchCertificate(value);
  }

  fetchCertificate(serialNumber: string) {
    this.certificateService.getBySerialNumber(serialNumber).subscribe({
      next: (data: Certificate) => {
        this.certificate = data;
      }
    })

  }


}
