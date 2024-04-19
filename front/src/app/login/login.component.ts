import { Component } from '@angular/core';
import {InputGroupModule} from "primeng/inputgroup";
import {PasswordModule} from "primeng/password";
import {ChipsModule} from "primeng/chips";
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    InputGroupModule,
    PasswordModule,
    ChipsModule,
    ButtonModule,
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string;
  password: string;

  constructor(private router: Router, private authService: AuthService) {
    this.email = "";
    this.password = "";

  }
  login(): void{
    if (this.email == "admin" && this.password == "admin"){
      this.router.navigate(['/certificates'])
      this.authService.setUser("admin")
    }
  }
}
