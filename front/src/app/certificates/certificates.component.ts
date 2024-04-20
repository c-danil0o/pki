import {Component, OnInit} from '@angular/core';
import {SplitterModule} from 'primeng/splitter'
import {TreeModule} from 'primeng/tree'
import {TreeNode} from "primeng/api";
import {FormatWidth, NgStyle} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {CertificateComponent} from "../certificate/certificate.component";
import {CertificateService} from "../services/certificate.service";
import {CertificateNode} from "../models/CertificateNode";
import {Certificate} from "../models/certificate";
import {arrayToTree, TreeItem} from "performant-array-to-tree";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {Subject} from "rxjs";
import {InputTextModule} from "primeng/inputtext";
import {DropdownChangeEvent, DropdownModule} from "primeng/dropdown";
import {CalendarModule} from "primeng/calendar";
import {CheckboxModule} from "primeng/checkbox";
import {BaseIcon} from "primeng/baseicon";

export interface CertificateItem {
  id: string,
  name: string,
  valid: boolean,
  icon: string
}

@Component({
  selector: 'app-certificates',
  standalone: true,
  imports: [
    SplitterModule,
    TreeModule,
    NgStyle,
    ButtonModule,
    CertificateComponent,
    CertificateComponent,
    DialogModule,
    FormsModule,
    InputTextModule,
    DropdownModule,
    CalendarModule,
    CheckboxModule,
  ],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})


export class CertificatesComponent implements OnInit {
  constructor(private certificateService: CertificateService) {
  }

  ngOnInit(): void {
    this.certificateService.getAllCertificateNodes().subscribe({
      next: (data: CertificateNode[]) => {
        for (let i = 0; i < data.length; i++) {
          if (data[i].alias === "root") {
            data[i].parentId = ""
          }
          data[i].key = data[i].id;
          data[i].label = data[i].subjectEmail
          data[i].checked = data[i].status === "VALID";
          switch (data[i].type) {
            case "END":
              data[i].icon = "pi pi-fw pi-id-card"
              break;
            case "INTERMEDIATE":
              data[i].icon = "pi pi-fw pi-pen-to-square"
              break;
            case "ROOT":
              data[i].icon = "pi pi-fw pi-hashtag"
              break;
          }
        }
        this.certificates = arrayToTree(data, {dataField: null});
        for (var i = 0; i < this.certificates.length; i++) {
          this.certificates
        }
        console.log(this.certificates)

      }
    })
  }

  certificates: CertificateNode[] = []
  selectedCertificate: CertificateNode | null = null
  issuerCertificate: Certificate | null = null;

  protected readonly FormatWidth = FormatWidth;
  addNewCertificateFormVisible: boolean = false;

  addCertificate() {
    if (this.selectedCertificate != null && this.selectedCertificate.type != "END") {
      this.addNewCertificateFormVisible = true;
    }
  }

  removeCertificate() {

  }

  protected readonly Subject = Subject;
  subjectFirstname: string = "";
  subjectLastname: string = "";
  subjectEmail: string = "";
  subjectOrganisation: string = "";
  subjectCountryCode: string = "";
  subjectAlias: string = "";
  certificateType: any;
  certificateTypes: any[] | undefined = [{name: "End entity", code: "END"}, {
    name: "End entity(HTTPS)",
    code: "END"
  }, {name: "End entity(DP)", code: "END"}, {
    name: "Intermediate",
    code: "INTERMEDIATE"
  }, {name: "Self-signed", code: "ROOT"}];
  validFrom: Date | null = null;
  validTo: Date | null = null;
  basicConstraints: boolean = false;
  subjectKeyIdentifier: boolean = false;
  authorityKeyIdentifier: boolean = false;
  digitalSignatureExtension: boolean = false;
  nonRepudiationExtension: boolean = false;
  keyEnciphermentExtension: boolean = false;
  dataEnciphermentExtension: boolean = false;
  keyAgreementExtension: boolean = false;
  keyCertSignExtension: boolean = false;
  crlSignExtension: boolean = false;
  encipherOnly: boolean = false;
  decipherOnly: boolean = false;

  deselectAll() {
    this.basicConstraints = false;
    this.subjectKeyIdentifier = false;
    this.authorityKeyIdentifier = false;
    this.digitalSignatureExtension = false;
    this.nonRepudiationExtension = false;
    this.keyEnciphermentExtension = false;
    this.dataEnciphermentExtension = false;
    this.keyAgreementExtension = false;
    this.keyCertSignExtension = false;
    this.crlSignExtension = false;
    this.encipherOnly = false;
    this.decipherOnly = false;
  }

  typeChanged($event: DropdownChangeEvent) {
    this.deselectAll();
    switch ($event.value.value) {
      case "End entity":
        this.subjectKeyIdentifier = true;
        this.authorityKeyIdentifier = true;
        break;
      case "End entity(HTTPS)":
        this.subjectKeyIdentifier = true;
        this.authorityKeyIdentifier = true;
        break;
      case "End entity(DP)":
        this.subjectKeyIdentifier = true;
        this.authorityKeyIdentifier = true;
        this.digitalSignatureExtension = true;
        this.keyEnciphermentExtension = true;
        this.dataEnciphermentExtension = true;
        break;
      case "Intermediate":
        this.subjectKeyIdentifier = true;
        this.basicConstraints = true;
        this.authorityKeyIdentifier = true;
        break;
      case "Self-signed":
        this.subjectKeyIdentifier = true;
        this.authorityKeyIdentifier = true;
        this.basicConstraints = true;
        break;
    }
  }

  generateNewCertificate() {

    if (this.subjectFirstname != "" && this.subjectLastname != "" && this.subjectEmail != "" &&
      this.subjectAlias != "" && this.subjectCountryCode != "" && this.subjectOrganisation != "") {
      if (this.certificateType != null && this.validFrom != null && this.validTo != null && this.validFrom < this.validTo) {
        console.log("cert")
      }
    }
  }
}
