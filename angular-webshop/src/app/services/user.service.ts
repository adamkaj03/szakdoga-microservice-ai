import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDTO} from "../models/user";

/**
 * a felhasználók lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = "http://localhost:8080";
  //private apiUrl = "https://proba-spring.azurewebsites.net";
  constructor(private http: HttpClient) { }

  /**
   * lekéri az összes usert egy get kérés segítségével a szervertől
   */
  public getUsers(): Observable<UserDTO[]>{
    return this.http.get<UserDTO[]>(this.apiUrl+"/api/felhasznalok");
  }

}
