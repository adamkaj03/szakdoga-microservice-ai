import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../models/book';
import { CartService } from '../services/cart.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-featured-carousel',
  templateUrl: './featured-carousel.component.html',
  styleUrls: ['./featured-carousel.component.css']
})
export class FeaturedCarouselComponent implements OnInit {
  
  // TODO: Replace with API call when slider endpoint is available
  // Ez egy dummy adat a carousel sliderhez, amíg nincs backend endpoint
  public featuredBooks: Book[] = [
    {
      id: 999,
      title: "A Gyűrűk Ura - A gyűrű szövetsége",
      author: "J.R.R. Tolkien",
      publishYear: 1954,
      price: 4990,
      category: { id: 1, name: "Fantasy" },
      imgUrl: "https://images.unsplash.com/photo-1621351183012-e2f9972dd9bf?w=800&h=600&fit=crop",
      description: "A legendás fantasy trilógia első része, amely egy varázslatos utazásra hívja az olvasót Középföldére, ahol hobbitok, tündék és varázslók küzdenek a gonosz ellen."
    },
    {
      id: 998,
      title: "1984",
      author: "George Orwell",
      publishYear: 1949,
      price: 3490,
      category: { id: 2, name: "Sci-fi" },
      imgUrl: "https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=800&h=600&fit=crop",
      description: "Orwell időtlen dystópiája egy totalitárius társadalomról, ahol a Nagy Testvér mindent lát. Egy olyan világ, ahol a gondolatbűn a legnagyobb vétség."
    },
    {
      id: 997,
      title: "A kis herceg",
      author: "Antoine de Saint-Exupéry",
      publishYear: 1943,
      price: 2990,
      category: { id: 3, name: "Klasszikus" },
      imgUrl: "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800&h=600&fit=crop",
      description: "Egy időtlen mese felnőtteknek és gyerekeknek egyaránt, amely a lényeges dolgokról szól az életben. Csak a szívével lát jól az ember..."
    }
  ];

  currentSlideIndex = 0;
  protected readonly encodeURIComponent = encodeURIComponent;

  constructor(
    private router: Router,
    private cartService: CartService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.startCarouselAutoSlide();
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
