import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService) {
  }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if(this.authService.isLoggedIn()){
      return true
    }
    //megadom, hogy az AuthGuard-ból jövök, hogy a signin page tudja
    //és eszerint tudjon információt adni a felhasználónak
    this.router.navigateByUrl("signin",  { state: { cameFromAuthGuard: true } })
    return false
  }
}
