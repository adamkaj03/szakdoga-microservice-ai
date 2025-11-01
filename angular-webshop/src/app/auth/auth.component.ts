import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {StorageService} from "../services/storage.service";
import {trigger} from "@angular/animations";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
  animations: [
    trigger('signUpButtonState', [

    ])
  ]
})
export class AuthComponent {
  isLoginMode = true;
  errorMessage: string = "";
  isCameFromAuthGuard: boolean = false;

  constructor(private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private toastr: ToastrService) {
  }

  ngOnInit(){
    const cameFromAuthGuard = history.state && history.state.cameFromAuthGuard;
    if (cameFromAuthGuard) {
      this.isCameFromAuthGuard = true
    }
  }

  onSwitchMode(){
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = ""
    this.isCameFromAuthGuard = false
  }

  onSubmit(authForm: NgForm) {
    this.isCameFromAuthGuard = false
    if(!authForm.valid){
      return;
    }
    //regisztráció
    if(!this.isLoginMode){
      this.doSignUp(authForm)
    }
    //bejelentkezés
    else{
      this.doSignIn(authForm)
    }
  }

  doSignUp(authForm: NgForm){
    const name = authForm.value.name;
    const username = authForm.value.username;
    const email = authForm.value.email;
    const password = authForm.value.password;
    this.authService.signUp(name, username, email, password).subscribe(resData =>{
      this.storageService.saveUser(resData);
      this.router.navigate(['/'])
      this.isLoginMode = false;
    }, error => {
      if (error.error === 'This email is already exist!') {
        this.errorMessage = 'Ez az e-mail cím már létezik!';
      } else if (error.error === 'This username is already exist!') {
        this.errorMessage = 'Ez a felhasználónév már létezik!';
      } else {
        this.errorMessage = 'Ismeretlen hiba történt!';
      }
      this.isLoginMode = false;
    });
  }

  doSignIn(authForm: NgForm){
    const username = authForm.value.username;
    const password = authForm.value.password;

    this.authService.signIn(username, password).subscribe(resData =>{
      this.storageService.saveUser(resData);
      this.router.navigate(['/'])
      this.toastr.success("Sikeres bejelentkezés!")
    }, error => {
      console.log(error)
      if (error.error === 'There is no user with this username!') {
        this.errorMessage = 'Nincs ilyen felhasználónévvel fiók a rendszerben!';
      }
      else{
        this.errorMessage = "Nem megfelelő a jelszó!"
      }
    });
  }

}
