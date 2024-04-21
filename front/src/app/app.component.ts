import {Component, OnInit} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {ButtonModule} from "primeng/button";
import {AuthService} from "./services/auth.service";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ButtonModule, ToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'certifikey';
  loggedIn: boolean;

  constructor(private authService: AuthService, private router: Router) {
    this.loggedIn = false;

  }

  ngOnInit(): void {
    this.authService.userState.subscribe((result: string) => {
      this.loggedIn = result != ""
    })
    }

  openCertificates() {
    this.router.navigate(['/certificates'])
  }

  openRequests() {
    this.router.navigate([
      '/requests'
    ])
  }
}
