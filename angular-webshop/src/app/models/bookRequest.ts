import {Category} from "./category";

/**
 * api kérésnél visszakapott Book-t reprezentálja, a szükséges adattagokkal
 */
export interface BookRequest{
  title: string;
  author: string;
  publishYear: number;
  price: number;
  category: Category;
  description: string;
}
