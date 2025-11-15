import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../models/book';
import { CartService } from '../services/cart.service';
import { ToastrService } from 'ngx-toastr';
import { BookService } from '../services/book.service';

@Component({
  selector: 'app-featured-carousel',
  templateUrl: './featured-carousel.component.html',
  styleUrls: ['./featured-carousel.component.css']
})
export class FeaturedCarouselComponent implements OnInit {
  
  public featuredBooks: Book[] = [];

  currentSlideIndex = 0;
  protected readonly encodeURIComponent = encodeURIComponent;

  constructor(
    private readonly router: Router,
    private readonly cartService: CartService,
    private readonly toastr: ToastrService,
    private readonly bookService: BookService
  ) {}

  ngOnInit(): void {
    this.loadLatestBooks();
    this.startCarouselAutoSlide();
  }

  loadLatestBooks(): void {
    this.bookService.getLatestBooks().subscribe({
      next: (books) => {
        this.featuredBooks = books;
      },
      error: (error) => {
        console.error('Error loading latest books:', error);
        this.toastr.error('Nem sikerült betölteni a legújabb könyveket');
      }
    });
  }

  startCarouselAutoSlide(): void {
    setInterval(() => {
      this.currentSlideIndex = (this.currentSlideIndex + 1) % this.featuredBooks.length;
    }, 5000); // Change slide every 5 seconds
  }

  goToSlide(index: number): void {
    this.currentSlideIndex = index;
  }

  navigateToBookPage(title: string): void {
    this.router.navigateByUrl(`book/` + encodeURIComponent(title));
  }

  addToCart(book: Book): void {
    this.cartService.addBookToCart(book, 1);
    this.toastr.success("Sikeresen beleraktál egy könyvet a kosárba!");
  }
}
