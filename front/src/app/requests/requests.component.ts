import {Component, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {TableModule, TableRowSelectEvent} from "primeng/table";
import {CertificateComponent} from "../certificate/certificate.component";
import {SplitterModule} from "primeng/splitter";
import {TreeModule} from "primeng/tree";
import {RequestComponent} from "../request/request.component";
import {Request} from "../models/Request";
import {RequestService} from "../services/request.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [
    ButtonModule,
    TableModule,
    CertificateComponent,
    SplitterModule,
    TreeModule,
    RequestComponent
  ],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.css'
})
export class RequestsComponent implements OnInit{
  constructor(private requestService: RequestService, private messageService: MessageService) {

  }
  ngOnInit(): void {
    this.refreshTable();
  }
  refreshTable(){
    this.requestService.getAllRequests().subscribe({
      next: (data:Request[])=>{
        this.csrRequests = data;
      }
    })
  }
  csrRequests: Request[] = [];
  loading: boolean = false;
  selectedRequest: Request | null = null;

  approveRequest() {

    if (this.selectedRequest != null){
      this.requestService.approveRequest(this.selectedRequest.requestId || 0).subscribe({
        next: () => {
          window.location.reload();
        }
      })
    }else{
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        key: 'bc',
        detail: 'No selected request!',
        life: 2000
      })

    }
  }


  onSelection($event: TableRowSelectEvent) {
    console.log($event)
  }
}
