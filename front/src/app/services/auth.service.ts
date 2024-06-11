import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user$ = new BehaviorSubject("");
  userState = this.user$.asObservable();
  constructor() { }


  refresh(){
    if (localStorage.getItem("user") != null)
      this.user$.next("admin")
  }
  setUser(user: string): void{
    localStorage.setItem("user", "admin")
    this.user$.next(user)
  }


  logout(){
    localStorage.removeItem("user");
    this.user$.next("")
  }
}
