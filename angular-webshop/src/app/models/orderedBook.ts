import {Order} from "./order";
import {Book} from "./book";

/**
 * api kérésnél visszakapott Order-t reprezentálja, a szükséges adattagokkal
 */
export interface OrderedBook{
  id: number;
  order: Order;
  book: Book;
  count: number;
}
