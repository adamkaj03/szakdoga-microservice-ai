import { Component } from '@angular/core';
import {AuthService} from "../services/auth.service";
import {StorageService} from "../services/storage.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html'
})
export class LogoutComponent {
  constructor(private authService: AuthService,
              private storageService: StorageService,
              private router: Router,
              private toastr: ToastrService) {
  }

  logout(){
    this.toastr.success("Sikeresen kijelentkezett!")
    if(this.storageService.getUserToken() != null){
      let token: string = <string>this.storageService.getUserToken();
      this.authService.logout(token);
      this.storageService.clean();
    }
    this.navigateToHome();
  }

  navigateToHome(){
    this.router.navigateByUrl("/")
  }
}
