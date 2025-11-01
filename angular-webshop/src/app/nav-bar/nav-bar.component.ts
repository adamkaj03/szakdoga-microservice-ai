import { Component } from '@angular/core';
import {StorageService} from "../services/storage.service";
import {Router} from "@angular/router";
import {CategoryService} from "../services/category.service";
import {Observable} from "rxjs";
import {Book} from "../models/book";
import {Category} from "../models/category";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {

  public storageServiceData: StorageService;
  public categories: Observable<Category[]>  = new Observable<Category[]>();
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
      this.storageService.saveCategory(id!);
    }
    else{
      this.storageService.cleanCategory();
    }
    this.router.navigateByUrl("/");
  }

  navigateToCart() {
    this.router.navigateByUrl("/cart");
  }

  onSubmit(searchForm: NgForm) {
    this.storageService.cleanCategory()
    const word = searchForm.value.search
    if(word !== null && word != ""){
      this.storageService.saveSearchWord(word)
    }
    else{
      this.storageService.cleanSeachWord()
    }
    location.reload()
  }
}
