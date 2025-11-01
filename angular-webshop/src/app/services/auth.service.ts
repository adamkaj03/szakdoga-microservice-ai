import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {UserAuth} from "../models/userAuth";
import {StorageService} from "./storage.service";


/**
 * az osztály felelőssége az authentikációval kapcsolatos hívásokat elküldeni a szervernek
 */
@Injectable({providedIn: 'root'})
export class AuthService {
  private apiUrl = "http://localhost:8080/api/auth";
  //private apiUrl = "https://proba-spring.azurewebsites.net/api/auth";
  constructor(private http: HttpClient, private storageService: StorageService) {}

  /**
   * regisztráció kérés, minden paramétert a body-ba rakok
   * @param name ilyen névvel akar regisztráni
   * @param username ilyen felhasználónévvel akar regisztráni
   * @param email ilyen emai címmel akar regisztráni
   * @param password ilyen jelszóval akar regisztráni
   */
  signUp(name: string, username: string, email: string, password: string){
    return this.http.post<UserAuth>(this.apiUrl + "/register", {
      "name" : name,
      "username" : username,
      "email" : email,
      "password" : password
    })
  }

  isLoggedIn(): boolean{
    return this.storageService.isLoggedIn()
  }

  /**
   * bejelentkezés kérés, minden paramétert a body-ba rakok
   * @param username ilyen felhasználónévvel akar bejelentkezni
   * @param password ilyen jelszóval akar bejelentkezni
   */
  signIn(username: string, password: string) {
    return this.http.post<UserAuth>(this.apiUrl + "/authenticate",{
      username : username,
      password : password
    });
  }

  getUserRole(){
    return this.storageService.getUserRole()
  }

  logout(token: string){
    return this.http.post(this.apiUrl + "/logout", {
      token
    });
  }
}
