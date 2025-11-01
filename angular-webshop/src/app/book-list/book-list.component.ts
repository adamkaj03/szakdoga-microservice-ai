import {Component, OnInit} from '@angular/core';
import {Book} from "../models/book";
import {HttpErrorResponse} from "@angular/common/http";
import {BookService} from "../services/book.service";
import {StorageService} from "../services/storage.service";
import {Observable} from "rxjs";
import {Router} from "@angular/router";
import {CartService} from "../services/cart.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit{
  isCartButtonWasClicked = false;
  public books: Observable<Book[]>  = new Observable<Book[]>();
  protected readonly encodeURIComponent = encodeURIComponent;

  constructor(
    private bookService: BookService,
    private router: Router,
    private storageService: StorageService,
    private cartService: CartService,
    private toastr: ToastrService) {
  }


  public getBooks() {
    let categoryId = this.storageService.getCategory()
    let searchWord = this.storageService.getSearchWord()
    if(categoryId !== null){
      this.books = this.bookService.getBooksByCategory(parseInt(categoryId, 10));
    }
    else if(searchWord !== null && searchWord != ""){
      this.books = this.bookService.getBooksBySearchingWord(searchWord!)
    }
    else{
      this.books = this.bookService.getBooks();
    }

  }

  ngOnInit(): void {
    this.getBooks();
  }

  navigateToBookPage(title: string) {
    if(!this.isCartButtonWasClicked){
      this.router.navigateByUrl(`book/` + encodeURIComponent(title));
    } else {
      this.isCartButtonWasClicked = false;
    }
  }

  isBookImgUrlNotNull(book: Book) {
    return book.imgUrl != null;
  }

  addToCart(book: Book) {
    this.isCartButtonWasClicked = true;
    this.cartService.addBookToCart(book, 1)
    this.toastr.success("Sikeresen beleraktál egy könyvet a kosárba!")
  }

  changeCursorStyle(event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    if (element) {
      element.style.cursor = 'pointer';
    }
  }

}
