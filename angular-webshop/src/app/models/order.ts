import {OrderedBook} from "./orderedBook";
import {UserDTO} from "./user";
import {ShippingType} from "./shippingType";

/**
 * api kérésnél visszakapott Order-t reprezentálja, a szükséges adattagokkal
 */
export interface Order{
  id: number;
  dateTime: string; // Ez egy string, mivel a LocalDateTime-t általában stringként kapod majd a backendtől
  deliveryAddress: string;
  userDTO: UserDTO;
  orderedBooks: OrderedBook[];
  shippingType: ShippingType;
  price: number;
}
