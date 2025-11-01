import {Category} from "./category";

/**
 * api kérésnél visszakapott Book-t reprezentálja, a szükséges adattagokkal
 */
export interface Book{
  id: number;
  title: string;
  author: string;
  publishYear: number;
  price: number;
  category: Category;
  imgUrl: string;
  description: string;
}
