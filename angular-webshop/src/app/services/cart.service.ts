import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "../models/category";
import {Cart} from "../models/cart";
import {Book} from "../models/book";
import {StorageService} from "./storage.service";


const CART_STRING = 'cart';

/**
 * a kategóriák lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = "http://localhost:8080";
  //private apiUrl = "https://proba-spring.azurewebsites.net";



  constructor(private http: HttpClient, private storageService: StorageService) { }



  public getCart(): Cart{
    let cartString = localStorage.getItem(CART_STRING)
    let cart: Cart = { cartContent: [], amount: 0 };
    if(cartString){
      try {
        cart = JSON.parse(cartString)
      } catch (error){
        console.error("Error while parsing cart ", error)
      }
    }
    return cart;
  }

  public putCart(cart: Cart): Observable<Cart>{
    return this.http.put<Cart>(this.apiUrl+"/api/kosar", cart);
  }



  public addBookToCart(book: Book, count: number){
    let cart = this.makeCartFromString()
    for(let i = 0; i < count; i++){
      cart.cartContent.push(book);
    }
    cart.amount = this.refreshPriceAmount(cart.cartContent)
    //this.putCart(cart).subscribe()
    this.addCartLocalStorage(cart)
  }

  refreshPriceAmount(books: Book[]): number{
    let amount = 0
    for(let i = 0; i <= books.length; i++){
      if(books.at(i) !== undefined){
        amount += books.at(i)!.price
      }
    }
    return amount
  }

  private makeCartFromString(){
    const cartString = localStorage.getItem(CART_STRING)
    let cart: Cart = { cartContent: [], amount: 0 };
    if(cartString){
      try {
        cart = JSON.parse(cartString)
        return cart;
      } catch (error){
        console.error("Error while parsing cart ", error)
      }
    }
    return cart;
  }
  getOneItemQuantity(): number {
    return 0;
  }

  getAllItemsQuantity(): number{
    return this.makeCartFromString().cartContent.length;
  }

  cleanCart(){
    localStorage.setItem(CART_STRING, '')
  }

  addCartLocalStorage(cart: Cart){
    localStorage.setItem(CART_STRING, JSON.stringify(cart))
  }
}
