import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../models/book';
import { BookService } from '../services/book.service';
import { CartService } from '../services/cart.service';
import { StorageService } from '../services/storage.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  public books: Book[] = [];
  public searchWord: string = "";
  protected readonly encodeURIComponent = encodeURIComponent;
  isCartButtonWasClicked = false;

  constructor(
    private bookService: BookService,
    private router: Router,
    private storageService: StorageService,
    private cartService: CartService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.searchWord = this.storageService.getSearchWord() || "";
    this.getSearchResults();
  }

  public getSearchResults(): void {
    const searchWord = this.storageService.getSearchWord();
    
    if (searchWord !== null && searchWord !== "") {
      this.bookService.getBooksBySearchingWord(searchWord).subscribe(books => {
        this.books = books;
      });
    } else {
      // Ha nincs keresési szó, akkor az összes könyvet mutatjuk
      this.bookService.getBooks().subscribe(books => {
        this.books = books;
      });
    }
  }

  navigateToBookPage(title: string): void {
    if (!this.isCartButtonWasClicked) {
      this.router.navigateByUrl(`book/` + encodeURIComponent(title));
    } else {
      this.isCartButtonWasClicked = false;
    }
  }

  isBookImgUrlNotNull(book: Book): boolean {
    return book.imgUrl != null;
  }

  addToCart(book: Book): void {
    this.isCartButtonWasClicked = true;
    this.cartService.addBookToCart(book, 1);
    this.toastr.success("Sikeresen beleraktál egy könyvet a kosárba!");
  }

  changeCursorStyle(event: MouseEvent): void {
    const element = event.currentTarget as HTMLElement;
    if (element) {
      element.style.cursor = 'pointer';
    }
  }

  public navigateToHome(event: Event): void {
    event.preventDefault();
    this.storageService.cleanCategory();
    this.storageService.cleanSeachWord();
    this.router.navigateByUrl("/").then(() => {
      window.location.reload();
    });
  }
}
