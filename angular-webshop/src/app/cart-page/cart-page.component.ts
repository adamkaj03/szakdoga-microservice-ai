import {Component, OnInit} from '@angular/core';
import {Cart} from "../models/cart";
import {CartService} from "../services/cart.service";
import {Book} from "../models/book";
import {Router} from "@angular/router";
import {ShippingType} from "../models/shippingType";
import {ShippingTypeService} from "../services/shippingType.service";
import {Observable} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css']
})
export class CartPageComponent implements OnInit{
  cart: Cart = {
    cartContent: [],
    amount: 0
  };
  shippingTypes: Observable<ShippingType[]> = new Observable<ShippingType[]>();

  selectedShippingType: ShippingType = {
    id: 0,
    name: "",
    price: 0
  };
  constructor(private cartService: CartService,
              private router: Router,
              private shippingTypeService: ShippingTypeService,
              private toastr: ToastrService) {
  }

  getCart(){
    return this.cartService.getCart()
  }

  getShippingType(){
    return this.shippingTypeService.getShippingTypes()
  }


  ngOnInit() {
    this.cart = this.getCart()
    this.shippingTypes = this.getShippingType()
    let defaultShippingType: ShippingType;
    this.shippingTypeService.getShippingTypeById(1).subscribe(
        (data) =>{
            defaultShippingType = data;
            this.selectedShippingType = defaultShippingType
            this.shippingTypeService.putShippingTypeToLocalStorage(defaultShippingType);
          }
        )
  }

  getCartQuantity(): number{
    return this.cartService.getOneItemQuantity();
  }

  getAllItemsQuantity(): number{
    return this.cartService.getAllItemsQuantity()
  }

  isCartEmpty() {
    if(this.cart.cartContent === undefined || this.cart.cartContent.length == 0){
      return true;
    }
    return false;
  }

  getFullPrice(): number|null {
    if(this.selectedShippingType.id == 0){
      return null
    }
    return this.cart.amount + this.selectedShippingType.price;
  }


  /**
   * elég egy üres metódust futtatni magától lefrissíti az adatokat
   */
  shippingSelectChange() {
    const shippingSelect = document.getElementById("shippingSelect") as HTMLSelectElement;
    const shippingId = parseFloat(shippingSelect.value) || 0;
    this.shippingTypes.subscribe(
      (shippingType) =>{
        for(let i = 0; i <= shippingType.length-1; i++){
          if(shippingId == shippingType.at(i)!.id){
            this.selectedShippingType = shippingType.at(i) || {
              id: 0,
              name: "",
              price: 0
            };
            break;
          }
        }
        this.shippingTypeService.putShippingTypeToLocalStorage(this.selectedShippingType)
      }

    )
  }

  navigateToShippingDetails() {
    this.router.navigateByUrl("shipping")
  }

  deleteFromCart(id: number) {
    for (let i = 0; i < this.cart.cartContent.length; i++){
      if(this.cart.cartContent.at(i)!.id == id){
        this.cart.cartContent.splice(i, 1)
        this.cart.amount = this.cartService.refreshPriceAmount(this.cart.cartContent)
        this.cartService.addCartLocalStorage(this.cart)
        break;
      }
    }
    location.reload()
  }
}
