import { Component, OnInit } from '@angular/core';
import {SplitterModule} from 'primeng/splitter'
import {TreeModule} from 'primeng/tree'
import {TreeNode} from "primeng/api";
import {FormatWidth, NgStyle} from "@angular/common";
import {ButtonModule} from "primeng/button";

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
  ],
  templateUrl: './certificates.component.html',
  styleUrl: './certificates.component.css'
})


export class CertificatesComponent implements OnInit{
  ngOnInit(): void {
    this.certificates = [
      {
       key : "123",
       label: "root cert",
       checked: true,
       icon: "icon.png",
        children: [
          {
            key : "1223",
            label: "ca cert",
            checked: true,
            icon: "icon.png",
            children: [
              {
                key : "12239",
                label: "end cert",
                checked: true,
                icon: "icon.png",
              },

              {
                key : "23",
                label: "end cert",
                checked: true,
                icon: "icon.png",

              }
            ]
          }
        ]
      },
    ]
  }

  certificates: TreeNode[] = []
  selectedCertificate : TreeNode | null = null

  protected readonly FormatWidth = FormatWidth;

  addCertificate() {

  }

  removeCertificate() {

  }
}
