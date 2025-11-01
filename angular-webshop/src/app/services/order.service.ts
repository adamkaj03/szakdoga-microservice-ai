import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {Order} from "../models/order";
import {StorageService} from "./storage.service";
import {Cart} from "../models/cart";
import {ShippingType} from "../models/shippingType";


/**
 * a rendelések lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = "http://localhost:8080";
  //private apiUrl = "https://proba-spring.azurewebsites.net";

  readonly headers = new HttpHeaders({
    'Authorization': 'Bearer ' + this.storageService.getUserToken()
  });

  constructor(private http: HttpClient, private storageService: StorageService) { }

  /**
   * lekéri az összes rendelést egy get kérés segítségével a szervertől
   */
  public getOrders(): Observable<Order[]>{
    return this.http.get<Order[]>(this.apiUrl+"/api/rendelesek",  { headers: this.headers });
  }


  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(this.apiUrl + "/api/rendelesek/" + id, { headers: this.headers })
  }

  public getPrice(id: number): Observable<number> {
    return this.getOrderById(id).pipe(
      map((data: Order) => {
        let price = 0;
        for (let i = 0; i < data.orderedBooks.length; i++) {
          price += data.orderedBooks.at(i)!.book.price * data.orderedBooks.at(i)!.count;
        }
        price += data.shippingType.price;
        return price;
      }),
      catchError(error => {
        console.log('Hiba:', error);
        return of(0);
      })
    );
  }


  postOrder(address: string, username: string, cart: Cart, shippingType: ShippingType) {
    this.http.post(this.apiUrl + "/api/rendelesek", {
      address: address,
      username: username,
      cart: cart,
      shippingType: shippingType
    }, { headers: this.headers}).subscribe()
  }
}
