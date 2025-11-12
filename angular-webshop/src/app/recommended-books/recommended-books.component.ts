import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../models/book';
import { CartService } from '../services/cart.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-recommended-books',
  templateUrl: './recommended-books.component.html',
  styleUrls: ['./recommended-books.component.css']
})
export class RecommendedBooksComponent {
  
  // TODO: Replace with recommendation API call when endpoint is available
  // Ez egy dummy adat az ajánlott könyvekhez, amíg nincs recommendation system
  public recommendedBooks: Book[] = [
    {
      id: 996,
      title: "Harry Potter és a bölcsek köve",
      author: "J.K. Rowling",
      publishYear: 1997,
      price: 3990,
      category: { id: 1, name: "Fantasy" },
      imgUrl: "https://images.unsplash.com/photo-1589998059171-988d887df646?w=400&h=500&fit=crop",
      description: "A világhírű varázslófiú kalandjainak első kötete."
    },
    {
      id: 995,
      title: "A Hobbit",
      author: "J.R.R. Tolkien",
      publishYear: 1937,
      price: 4290,
      category: { id: 1, name: "Fantasy" },
      imgUrl: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400&h=500&fit=crop",
      description: "Bilbó Baggins kalandos utazása a Magányos Hegyhez."
    },
    {
      id: 994,
      title: "Állatfarm",
      author: "George Orwell",
      publishYear: 1945,
      price: 2790,
      category: { id: 2, name: "Klasszikus" },
      imgUrl: "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400&h=500&fit=crop",
      description: "Politikai szatíra egy farmon fellázadó állatokról."
    },
    {
      id: 993,
      title: "A Mester és Margarita",
      author: "Mihail Bulgakov",
      publishYear: 1967,
      price: 4490,
      category: { id: 3, name: "Klasszikus" },
      imgUrl: "https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=400&h=500&fit=crop",
      description: "Szatirikus fantasy a Moszkvába látogató Sátánról."
    }
  ];

  protected readonly encodeURIComponent = encodeURIComponent;
  isCartButtonWasClicked = false;

  constructor(
    private router: Router,
    private cartService: CartService,
    private toastr: ToastrService
  ) {}

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
