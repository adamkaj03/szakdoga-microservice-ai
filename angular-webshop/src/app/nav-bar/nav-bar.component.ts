import { Component } from '@angular/core';
import {StorageService} from "../services/storage.service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {CategoryService} from "../services/category.service";
import {Observable} from "rxjs";
import {Category} from "../models/category";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {

  public storageServiceData: StorageService;
  public categories: Observable<Category[]> = new Observable<Category[]>();
  searchWord = ""
  constructor(
    private router: Router,
    private storageService: StorageService,
    private categoryService: CategoryService)
  {
    this.storageServiceData = this.storageService.getData();
    this.categories = this.categoryService.getCategories();
  }


  navigateToSignIn(){
    this.router.navigateByUrl("/signin");
  }

  navigateToHome(event: Event){
    event.preventDefault();
    this.storageService.cleanCategory();
    this.storageService.cleanSeachWord();
    this.router.navigateByUrl("/").then(() => {
      window.location.reload();
    });
  }

  isUserAdmin(): boolean {
    if(this.storageServiceData.getUserRole() != null && this.storageServiceData.getUserRole() === "ADMIN"){
      return true;
    }
    return false;
  }

  isUserSignedIn() {
    if(this.storageServiceData.getUsername() != null){
      return true;
    }
    return false;
  }

  refreshWithCategory(id: number|null) {
    this.storageService.cleanSeachWord()
    if(id !== null){
      this.storageService.saveCategory(id);
    }
    else{
      this.storageService.cleanCategory();
    }
    this.router.navigateByUrl("/").then(() => {
      window.location.reload();
    });
  }

  navigateToCart() {
    this.router.navigateByUrl("/cart");
  }

  onSubmit(searchForm: NgForm) {
    this.storageService.cleanCategory()
    const word = searchForm.value.search
    if(word !== null && word != ""){
      this.storageService.saveSearchWord(word)
      this.router.navigateByUrl("/search").then(() => {
        window.location.reload();
      });
    }
    else{
      this.storageService.cleanSeachWord()
    }
  }
}
