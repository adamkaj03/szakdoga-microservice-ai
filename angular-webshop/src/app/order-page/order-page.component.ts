import { Component } from '@angular/core';
import {map, Observable} from "rxjs";
import {OrderService} from "../services/order.service";
import {Order} from "../models/order";
import {OrderedBook} from "../models/orderedBook";

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.css']
})
export class OrderPageComponent {
  public orders: Observable<Order[]>  = new Observable<Order[]>();
  public orderPrice: number;
  

  constructor(private orderService: OrderService) {
    this.orderPrice = 0;
  }

  public getOrders() {
      this.orders = this.orderService.getOrders();
  }

  ngOnInit(): void {
    this.getOrders();
    this.orderService.getOrders().subscribe((data) => {
      console.log("Lekért rendelések:", data);
    });
    
  }

  getOrderBookTitle(orderBooks: OrderedBook[]): string {
    if (!orderBooks || orderBooks.length === 0) {
      return '—';
    }
  
    return orderBooks
      .map(book => `${book.book.title} (${book.count} db)`)
      .join('; ');
  }
  
}
