import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {CategoryService} from "../services/category.service";
import {Observable} from "rxjs";
import {Category} from "../models/category";
import {BookService} from "../services/book.service";
import {HttpHeaders} from "@angular/common/http";
import {StorageService} from "../services/storage.service";
import {Book} from "../models/book";
import {BookRequest} from "../models/bookRequest";

@Component({
  selector: 'app-new-item-page',
  templateUrl: './new-item-page.component.html',
  styleUrls: ['./new-item-page.component.css']
})
export class NewItemPageComponent {
  isNewCategoryMode: boolean = true;
  errorMessageCategory: string = ""
  successMessageCategory: string = "";
  errorMessageBook: string = ""
  successMessageBook: string = ""
  public categories: Observable<Category[]>  = new Observable<Category[]>();
  selectedFile: File | null = null;

  constructor(private categoryService: CategoryService, private bookService: BookService) {
    this.categories = this.categoryService.getCategories();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target!.files[0];
  }

  onSubmit(newItemForm: NgForm) {
    const title = newItemForm.value.title
    const author = newItemForm.value.author
    const publishYear = newItemForm.value.publishYear
    const price = newItemForm.value.price
    const desc = newItemForm.value.description
    const categoryName = newItemForm.value.categoryName
    let selectedCategory: Category = {
      id: 0,
      name: ""
    }


    if(this.isNewCategoryMode){
      this.errorMessageCategory = ""
      this.successMessageCategory = ""
      this.categoryService.postCategory(categoryName).subscribe(
          (response) =>{
            this.successMessageCategory = "Új kategória sikeresen fel lett véve!"
              },
          (error) =>{
                this.errorMessageCategory = "A megadott kategória már szerepel a rendszerben!"
              }
      )
    }
    else{
      this.errorMessageBook = ""
      this.successMessageBook = ""
      const categorySelect = document.getElementById("categorySelect") as HTMLSelectElement;
      const categoryId = parseFloat(categorySelect.value) || 0;

      this.categoryService.getCategories().subscribe(
        (categoryType) => {
          selectedCategory = categoryType.find(category => category.id === categoryId) || {
            id: 0,
            name: ""
          };
          let book: BookRequest = {
            title: title,
            author: author,
            publishYear: publishYear,
            price: price,
            category: selectedCategory,
            description: desc
          }
          this.bookService.postBook(book, this.selectedFile).subscribe(
            (response) =>{
              this.successMessageBook = "Sikeresen felvettél egy új könyvet a rendszerbe!"
            },
            (error) =>{
              this.errorMessageBook = "Nem sikerült felvenni a könyvet, mert ilyen már van a rendszerben!"
            }
          )
        }
      );

    }
  }

  onSwitchMode() {
    this.isNewCategoryMode = !this.isNewCategoryMode
    this.errorMessageCategory = ""
    this.successMessageCategory = ""
    this.errorMessageBook = ""
    this.successMessageBook = ""
  }


}
