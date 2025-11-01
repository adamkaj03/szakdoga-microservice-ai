import {Book} from "./book";

/**
 * api kérésnél visszakapott Cart-ot reprezentálja, a szükséges adattagokkal
 */
export interface Cart{
  cartContent: Book[];
  amount: number;
}
