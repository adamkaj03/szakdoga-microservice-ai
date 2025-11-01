import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable()
export class RoleGuard implements CanActivate {
  constructor(public authService: AuthService, public router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data["expectedRole"];
    const role = this.authService.getUserRole()
    if(role !== null && expectedRole === role && this.authService.isLoggedIn()){
      return true;
    }
    this.router.navigateByUrl("forbidden")
    return false;

  }
}
