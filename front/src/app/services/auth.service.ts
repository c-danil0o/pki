import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user$ = new BehaviorSubject("");
  userState = this.user$.asObservable();
  constructor() { }


  setUser(user: string): void{
    this.user$.next(user)
  }
}
