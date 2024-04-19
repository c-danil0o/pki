import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {CertificatesComponent} from "./certificates/certificates.component";
import {RequestsComponent} from "./requests/requests.component";

export const routes: Routes = [{
  component: LoginComponent, path: ""
}, {
  component: CertificatesComponent, path: "certificates"
},{
  component: RequestsComponent, path: "requests"
}];
