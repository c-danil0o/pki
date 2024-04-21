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
import {generate, Subject} from "rxjs";
import {InputTextModule} from "primeng/inputtext";
import {DropdownChangeEvent, DropdownModule} from "primeng/dropdown";
import {CalendarModule} from "primeng/calendar";
import {CheckboxModule} from "primeng/checkbox";
import {BaseIcon} from "primeng/baseicon";
import {Request} from "../models/Request";
import {SelectButtonChangeEvent, SelectButtonModule} from "primeng/selectbutton";
import {InputTextareaModule} from "primeng/inputtextarea";
import {ReturnStatement} from "@angular/compiler";

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
    SelectButtonModule,
    InputTextareaModule,
  ],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})


export class CertificatesComponent implements OnInit {
  constructor(private certificateService: CertificateService) {
  }

  ngOnInit(): void {
    this.refreshTree()
  }

  refreshTree() {

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
  certificateTypes: any = [{name: "End entity", code: "END"}, {
    name: "End entity(HTTPS)",
    code: "END"
  }, {name: "End entity(DP)", code: "END"}, {
    name: "Intermediate",
    code: "INTERMEDIATE"
  }];
  validFrom: Date | null = null;
  validTo: Date | null = null;
  extensions: any = {
    basicConstraints: false,
    subjectKeyIdentifier: false,
    authorityKeyIdentifier: false,
  }
  keyUsages: any = {
    digitalSignatureExtension: false,
    nonRepudiationExtension: false,
    keyEnciphermentExtension: false,
    dataEnciphermentExtension: false,
    keyAgreementExtension: false,
    keyCertSignExtension: false,
    crlSignExtension: false,
    encipherOnly: false,
    decipherOnly: false
  }
  keyUsageCodes: any = {
    digitalSignatureExtension: 128,
    nonRepudiationExtension: 64,
    keyEnciphermentExtension: 32,
    dataEnciphermentExtension: 16,
    keyAgreementExtension: 8,
    keyCertSignExtension: 4,
    crlSignExtension: 2,
    encipherOnly: 1,
    decipherOnly: 32768

  }
  selectedKeyOption: any;
  keyOptions: any[] | undefined = [{label: "Insert", value: "insert"}, {label: "Generate", value: "generate"}];
  publicKey: string = "";
  keyInsertSelected: boolean = false;
  keyGenerateSelected: boolean = false;
  privateKey: string = "";



  deselectAll() {
    this.extensions.basicConstraints = false;
    this.extensions.subjectKeyIdentifier = false;
    this.extensions.authorityKeyIdentifier = false;
    this.keyUsages.digitalSignatureExtension = false;
    this.keyUsages.nonRepudiationExtension = false;
    this.keyUsages.keyEnciphermentExtension = false;
    this.keyUsages.dataEnciphermentExtension = false;
    this.keyUsages.keyAgreementExtension = false;
    this.keyUsages.keyCertSignExtension = false;
    this.keyUsages.crlSignExtension = false;
    this.keyUsages.encipherOnly = false;
    this.keyUsages.decipherOnly = false;
  }

  typeChanged($event: DropdownChangeEvent) {
    this.deselectAll();
    switch ($event.value.name) {
      case "End entity":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.authorityKeyIdentifier = true;
        break;
      case "End entity(HTTPS)":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.authorityKeyIdentifier = true;
        break;
      case "End entity(DP)":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.authorityKeyIdentifier = true;
        this.keyUsages.digitalSignatureExtension = true;
        this.keyUsages.keyEnciphermentExtension = true;
        this.keyUsages.dataEnciphermentExtension = true;
        break;
      case "Intermediate":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.basicConstraints = true;
        this.extensions.authorityKeyIdentifier = true;
        break;
    }
  }

  generateKeyPair() {
    window.crypto.subtle.generateKey({
        name: "RSA-OAEP",
        modulusLength: 2048,
        publicExponent: new Uint8Array([1, 0, 1]),
        hash: "SHA-256",
      },
      true,
      ["encrypt", "decrypt"]).then(function (keys) {
      return [window.crypto.subtle.exportKey("spki", keys.publicKey), window.crypto.subtle.exportKey("pkcs8", keys.privateKey)];
    }).then(async (data) => {
        console.log(data[0])
        this.publicKey = await data[0].then(value => this.spkiToPEMPublic(value))
        this.privateKey = await data[1].then(value => this.spkiToPEMPrivate(value))
      }
    ).catch(function (err) {
      console.log(err)
    })
  }

  spkiToPEMPublic(keydata: any) {
    var keydataS = this.arrayBufferToString(keydata);
    var keydataB64 = window.btoa(keydataS);
    var keydataB64Pem = this.formatAsPem(keydataB64);
    return keydataB64Pem;
  }

  spkiToPEMPrivate(keydata: any) {
    var keydataS = this.arrayBufferToString(keydata);
    var keydataB64 = window.btoa(keydataS);
    var keydataB64Pem = this.formatAsPemPrivate(keydataB64);
    return keydataB64Pem;
  }

  arrayBufferToString(buffer: any) {
    var binary = '';
    var bytes = new Uint8Array(buffer);
    var len = bytes.byteLength;
    for (var i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return binary;
  }

  formatAsPem(str: string) {
    var finalString = '-----BEGIN PUBLIC KEY-----\n';

    while (str.length > 0) {
      finalString += str.substring(0, 64) + '\n';
      str = str.substring(64);
    }

    finalString = finalString + "-----END PUBLIC KEY-----";

    return finalString;
  }

  formatAsPemPrivate(str: string) {
    var finalString = '-----BEGIN PRIVATE KEY-----\n';

    while (str.length > 0) {
      finalString += str.substring(0, 64) + '\n';
      str = str.substring(64);
    }

    finalString = finalString + "-----END PRIVATE KEY-----";

    return finalString;
  }

  packExtensions(): object {
    return {
      "basic": String(this.extensions.basicConstraints),
      "subjectKeyIdentifier": String(this.extensions.subjectKeyIdentifier),
      "authorityKeyIdentifier": String(this.extensions.authorityKeyIdentifier),
      "keyUsage": this.getKeyUsages()
    }
  }

  getKeyUsages() {
    let keyUsages = [];
    for (const [key, value] of Object.entries(this.keyUsages)) {
      if (value) {
        keyUsages.push(this.keyUsageCodes[key])
      }
    }
    return keyUsages.join(",");
  }

  generateNewCertificate() {

    if (this.subjectFirstname != "" && this.subjectLastname != "" && this.subjectEmail != "" &&
      this.subjectAlias != "" && this.subjectCountryCode != "" && this.subjectOrganisation != "") {
      console.log("123")
      console.log(this.certificateType)
      console.log(this.validFrom)
      console.log(this.validTo)
      console.log(this.publicKey)
      console.log(this.validFrom?.getTime())
      console.log(this.validTo?.getTime());
      console.log(new Date().getTime())
      if (this.certificateType != null && this.validFrom != null && this.validTo != null && this.validFrom.getTime() < this.validTo.getTime() && this.validFrom.getTime() > new Date().getTime() && this.publicKey != "") {
        let request: Request = {
          firstName: this.subjectFirstname,
          lastName: this.subjectLastname,
          email: this.subjectEmail,
          organisation: this.subjectOrganisation,
          countryCode: this.subjectCountryCode,
          alias: this.subjectAlias,
          signerAlias: this.selectedCertificate?.alias || "",
          validTo: this.validTo,
          validFrom: this.validFrom,
          type: this.certificateType.code,
          extensions: this.packExtensions(),
          publicKey: this.publicKey,
        }

        console.log(request)
        this.certificateService.generateNewCertificate(request).subscribe({
          next: (data: Certificate) => {

            console.log(data)

            window.location.reload()
          },
          error: (err) => console.log(err)

        })
      }
    }
  }

  keyOptionChanged($event: SelectButtonChangeEvent) {
    this.keyInsertSelected = false;
    this.keyGenerateSelected = false;
    if ($event.value === "insert") {
      this.keyInsertSelected = true;
    }
    if ($event.value === "generate") {
      this.keyGenerateSelected = true;
    }

  }
}
