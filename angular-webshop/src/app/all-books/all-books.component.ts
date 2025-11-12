import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Book } from '../models/book';
import { BookService } from '../services/book.service';
import { CartService } from '../services/cart.service';
import { StorageService } from '../services/storage.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-all-books',
  templateUrl: './all-books.component.html',
  styleUrls: ['./all-books.component.css']
})
export class AllBooksComponent implements OnInit {
  public books: Observable<Book[]> = new Observable<Book[]>();
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
    this.getBooks();
  }

  public getBooks(): void {
    const categoryId = this.storageService.getCategory();
    const searchWord = this.storageService.getSearchWord();
    
    if (categoryId !== null) {
      this.books = this.bookService.getBooksByCategory(parseInt(categoryId, 10));
    } else if (searchWord !== null && searchWord !== "") {
      this.books = this.bookService.getBooksBySearchingWord(searchWord);
    } else {
      this.books = this.bookService.getBooks();
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
}
