import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Book} from "../models/book";
import {ShippingType} from "../models/shippingType";
import {Cart} from "../models/cart";

const SHIPPING_TYPE_STRING = 'shipping-type';

@Injectable({
  providedIn: 'root'
})
export class ShippingTypeService {
  private apiUrl = "http://localhost:8080";
  //private apiUrl = "https://proba-spring.azurewebsites.net";

  constructor(private http: HttpClient) {
  }

  public getShippingTypes(): Observable<ShippingType[]> {
    return this.http.get<ShippingType[]>(this.apiUrl + "/api/szallitasi_tipus");
  }

  public getShippingTypeById(id: number){
    return this.http.get<ShippingType>(this.apiUrl + "/api/szallitasi_tipus/" + id);
  }


  putShippingTypeToLocalStorage(shippingType: ShippingType) {
    localStorage.setItem(SHIPPING_TYPE_STRING, JSON.stringify(shippingType))
  }

  getShippingTypeFromLocal() {
    let shippingTypeString = localStorage.getItem(SHIPPING_TYPE_STRING)
    let shippingType: ShippingType = { id: 0, name: "", price: 0 };
    if(shippingTypeString){
      try {
        shippingType = JSON.parse(shippingTypeString)
      } catch (error){
        console.error("Error while parsing cart ", error)
      }
    }
    return shippingType;
  }
}
