import { Component, OnInit } from '@angular/core';
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

export interface CertificateItem{
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
  ],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})


export class CertificatesComponent implements OnInit{
  constructor(private certificateService: CertificateService) {
  }
  ngOnInit(): void {
    this.certificateService.getAllCertificateNodes().subscribe({
      next: (data: CertificateNode[]) => {
        for (let i=0; i<data.length; i++){
          if (data[i].alias === "root"){
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
        this.certificates = arrayToTree(data,  { dataField: null});
        for (var i = 0; i<this.certificates.length; i++){
          this.certificates
        }
        console.log(this.certificates)

      }
    })
  //   this.certificates = [
  //     {
  //      key : "123",
  //      label: "root cert",
  //      checked: true,
  //      icon: "icon.png",
  //       children: [
  //         {
  //           key : "1223",
  //           label: "ca cert",
  //           checked: true,
  //           icon: "icon.png",
  //           children: [
  //             {
  //               key : "12239",
  //               label: "end cert",
  //               checked: true,
  //               icon: "icon.png",
  //             },
  //
  //             {
  //               key : "23",
  //               label: "end cert",
  //               checked: true,
  //               icon: "icon.png",
  //
  //             }
  //           ]
  //         }
  //       ]
  //     },
  //   ]
  }

  certificates: CertificateNode[] = []
  selectedCertificate : CertificateNode | null = null

  protected readonly FormatWidth = FormatWidth;

  addCertificate() {

  }

  removeCertificate() {

  }
}
