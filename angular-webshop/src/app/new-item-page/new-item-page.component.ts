import { Component } from '@angular/core';
import { NgForm } from "@angular/forms";
import { CategoryService } from "../services/category.service";
import { Observable } from "rxjs";
import { Category } from "../models/category";
import { BookService } from "../services/book.service";
import { BookRequest } from "../models/bookRequest";
import { BookDataResponse } from "../models/bookDataResponse";

@Component({
  selector: 'app-new-item-page',
  templateUrl: './new-item-page.component.html',
  styleUrls: ['./new-item-page.component.css']
})
export class NewItemPageComponent {
  isNewCategoryMode = true;
  errorMessageCategory = "";
  successMessageCategory = "";
  errorMessageBook = "";
  successMessageBook = "";
  errorMessageAI = "";
  
  public categories: Observable<Category[]> = new Observable<Category[]>();
  selectedFile: File | null = null;
  imagePreviewUrl: string | null = null;
  isLoadingAI = false;

  constructor(
    private categoryService: CategoryService,
    private bookService: BookService
  ) {
    this.categories = this.categoryService.getCategories();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      this.createImagePreview(this.selectedFile);
    }
  }

  private createImagePreview(file: File): void {
    const reader = new FileReader();
    reader.onload = (e: ProgressEvent<FileReader>) => {
      this.imagePreviewUrl = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }

  onGenerateAIDescription(form: NgForm): void {
    if (!this.selectedFile) {
      this.errorMessageAI = "Kérlek, először válassz egy képet!";
      return;
    }

    this.clearMessages();
    this.isLoadingAI = true;
    
    this.bookService.getBookDataByImage(this.selectedFile).subscribe({
      next: (response: BookDataResponse) => {
        this.isLoadingAI = false;
        this.handleAIResponse(response, form);
      },
      error: (error) => {
        this.isLoadingAI = false;
        this.errorMessageAI = "Hiba történt az AI leírás generálás közben. Kérlek, írd be az adatokat kézzel!";
      }
    });
  }

  private handleAIResponse(response: BookDataResponse, form: NgForm): void {
    if (response.error && response.error.trim() !== "") {
      this.errorMessageAI = "Hiba történt az AI leírás generálás közben. Kérlek, írd be az adatokat kézzel!";
      return;
    }

    form.controls['title']?.setValue(response.title);
    form.controls['author']?.setValue(response.author);
    form.controls['description']?.setValue(response.description);
  }

  onSubmit(newItemForm: NgForm): void {
    if (this.isNewCategoryMode) {
      this.submitNewCategory(newItemForm.value.categoryName);
    } else {
      this.submitNewBook(newItemForm);
    }
  }

  private submitNewCategory(categoryName: string): void {
    this.clearMessages();
    
    this.categoryService.postCategory(categoryName).subscribe({
      next: () => {
        this.successMessageCategory = "Új kategória sikeresen fel lett véve!";
      },
      error: () => {
        this.errorMessageCategory = "A megadott kategória már szerepel a rendszerben!";
      }
    });
  }

  private submitNewBook(form: NgForm): void {
    this.clearMessages();
    
    const categoryId = this.getSelectedCategoryId();
    
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        const selectedCategory = this.findCategoryById(categories, categoryId);
        const bookRequest = this.createBookRequest(form.value, selectedCategory);
        this.postBook(bookRequest);
      }
    });
  }

  private getSelectedCategoryId(): number {
    const categorySelect = document.getElementById("categorySelect") as HTMLSelectElement;
    return parseFloat(categorySelect.value) || 0;
  }

  private findCategoryById(categories: Category[], categoryId: number): Category {
    return categories.find(category => category.id === categoryId) || {
      id: 0,
      name: ""
    };
  }

  private createBookRequest(formValue: any, category: Category): BookRequest {
    return {
      title: formValue.title,
      author: formValue.author,
      publishYear: formValue.publishYear,
      price: formValue.price,
      category: category,
      description: formValue.description
    };
  }

  private postBook(bookRequest: BookRequest): void {
    this.bookService.postBook(bookRequest, this.selectedFile).subscribe({
      next: () => {
        this.successMessageBook = "Sikeresen felvettél egy új könyvet a rendszerbe!";
      },
      error: () => {
        this.errorMessageBook = "Nem sikerült felvenni a könyvet, mert ilyen már van a rendszerben!";
      }
    });
  }

  onSwitchMode(): void {
    this.isNewCategoryMode = !this.isNewCategoryMode;
    this.clearMessages();
    this.clearImageSelection();
  }

  private clearMessages(): void {
    this.errorMessageCategory = "";
    this.successMessageCategory = "";
    this.errorMessageBook = "";
    this.successMessageBook = "";
    this.errorMessageAI = "";
  }

  private clearImageSelection(): void {
    this.selectedFile = null;
    this.imagePreviewUrl = null;
  }
}
