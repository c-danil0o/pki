import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {SplitterModule} from 'primeng/splitter'
import {TreeModule} from 'primeng/tree'
import {MessageService, TreeNode} from "primeng/api";
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
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

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
  constructor(private authService: AuthService, private router: Router, private certificateService: CertificateService, private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.authService.userState.subscribe({
      next: value => {if (value == "") this.router.navigate(['/'])}
    })
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
      this.signerAlias = this.selectedCertificate.alias || "";
      this.addNewCertificateFormVisible = true;
      this.certificateTypes = [{name: "End entity", code: "END"}, {
        name: "End entity(HTTPS)",
        code: "END"
      }, {name: "End entity(DP)", code: "END"}, {
        name: "Intermediate",
        code: "INTERMEDIATE"
      }];
    }else{
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        key: 'bc',
        detail: 'Please select certificate!',
        life: 2000
      })


    }
  }
  addRootCertificate(){
    if (this.certificates.length == 0){
      // dodati toast
      this.certificateTypes = [{name: "Self-signed", code: "ROOT"}]
      this.subjectAlias = "root";
      this.signerAlias = "root";
      this.subjectAliasDisabled = true;
      this.rootCertificate = true;
      this.addNewCertificateFormVisible = true;
    }else{
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        key: 'bc',
        detail: 'Root already exists!',
        life: 2000
      })

    }
  }

  removeCertificate() {
    if (this.selectedCertificate != null){
      this.certificateService.deleteCertificate(this.selectedCertificate.id || "").subscribe({
        next: () => {
          window.location.reload();
        }
      })
    }else{
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        key: 'bc',
        detail: 'No selected certificate!',
        life: 2000
      })

    }
  }

  protected readonly Subject = Subject;
  subjectAliasDisabled: boolean = false;
  subjectFirstname: string = "";
  subjectLastname: string = "";
  subjectEmail: string = "";
  subjectOrganisation: string = "";
  subjectCountryCode: string = "";
  subjectAlias: string = "";
  signerAlias: string = "";
  certificateType: any;
  certificateTypes: any  ;
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
  keysEnabled: boolean = false;
  rootCertificate: boolean = false;



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
        this.keysEnabled = true;
        break;
      case "End entity(HTTPS)":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.authorityKeyIdentifier = true;
        this.keyUsages.digitalSignatureExtension = true;
        this.keyUsages.keyEnciphermentExtension = true;
        this.keysEnabled = true;
        break;
      case "End entity(DP)":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.authorityKeyIdentifier = true;
        this.keyUsages.digitalSignatureExtension = true;
        this.keyUsages.keyEnciphermentExtension = true;
        this.keyUsages.dataEnciphermentExtension = true;
        this.keysEnabled = true;
        break;
      case "Intermediate":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.basicConstraints = true;
        this.extensions.authorityKeyIdentifier = true;
        this.keysEnabled = false;
        this.publicKey = "";
        break;
      case  "Self-signed":
        this.extensions.subjectKeyIdentifier = true;
        this.extensions.basicConstraints = true;
        this.keysEnabled = false;
        this.publicKey = "";
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
      if (this.certificateType != null && this.validFrom != null && this.validTo != null && this.validFrom.getTime() < this.validTo.getTime() && this.validFrom.getTime()+ 86400000 >= new Date().getTime()) {
        let request: Request = {
          firstName: this.subjectFirstname,
          lastName: this.subjectLastname,
          email: this.subjectEmail,
          organisation: this.subjectOrganisation,
          countryCode: this.subjectCountryCode,
          alias: this.subjectAlias,
          signerAlias: this.signerAlias,
          validTo: this.validTo,
          validFrom: this.validFrom,
          type: this.certificateType.code,
          extensions: this.packExtensions(),
          publicKey: this.publicKey === "" ? undefined : this.publicKey,
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
