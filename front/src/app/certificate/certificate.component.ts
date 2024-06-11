import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
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
  styleUrl: './certificate.component.css',
  encapsulation: ViewEncapsulation.None,
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

  syntaxHighlight(json: string) {
    if (typeof json != 'string') {
      json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
      var cls = 'number';
      if (/^"/.test(match)) {
        if (/:$/.test(match)) {
          cls = 'key';
        } else {
          cls = 'string';
        }
      } else if (/true|false/.test(match)) {
        cls = 'boolean';
      } else if (/null/.test(match)) {
        cls = 'null';
      }
      return '<span class="' + cls + '">' + match + '</span>';
    });
  }


  protected readonly JSON = JSON;
}
