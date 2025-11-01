import { Component } from '@angular/core';
import {CartService} from "../services/cart.service";
import {Router} from "@angular/router";
import {OrderService} from "../services/order.service";
import {NgForm} from "@angular/forms";
import {UserService} from "../services/user.service";
import {StorageService} from "../services/storage.service";
import {ShippingTypeService} from "../services/shippingType.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-shipping-details',
  templateUrl: './shipping-details.component.html',
  styleUrls: ['./shipping-details.component.css']
})
export class ShippingDetailsComponent {

  constructor(private cartService: CartService,
              private router: Router,
              private orderService: OrderService,
              private storgeService: StorageService,
              private shippingTypeService: ShippingTypeService,
              private toastr: ToastrService) {
  }

  orderSubmit(shippingForm: NgForm) {
    const address = shippingForm.value.postCode + ", " + shippingForm.value.city + ", " + shippingForm.value.street
    this.orderService.postOrder(address, this.storgeService.getUsername() || "", this.cartService.getCart(), this.shippingTypeService.getShippingTypeFromLocal())
    this.cartService.cleanCart()
    this.router.navigateByUrl("")
    this.toastr.success("Köszönjük a rendelését!")
  }
}
