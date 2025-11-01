import { Injectable } from '@angular/core';
import {UserAuth} from "../models/userAuth";
import {Book} from "../models/book";

/**
 * konstansok a kulcsok, melyek segítségével elérem az adatokat a localstorageból
 */
const USER_NAME = 'auth-username';
const USER_ROLE = 'auth-role';
const USER_TOKEN = 'auth-token';
const CATEGORY_ID = 'category-id';
const CART = 'cart';
const SEARCH_WORD = 'search-word';

/**
 * injektálható, mert sok helyen akarom használni
 */
@Injectable({
  providedIn: 'root'
})

/**
 * a service felelőssége, az bejelentkezéskor visszakapott tokent, role-t, illetve felhasználónevet eltárolni
 */
export class StorageService {
  constructor() {}

  /**
   * kijelentkezéskor kitisztítja ezt a tárólót
   */
  clean(): void {
    localStorage.removeItem(USER_NAME);
    localStorage.removeItem(USER_ROLE);
    localStorage.removeItem(USER_TOKEN);
  }

  cleanCategory(): void {
    localStorage.removeItem(CATEGORY_ID);
  }

  /**
   *
   * @param userAuth kap egy UserAuth típusú objektumot, melynek minden adattágját
   * elmenti ebben a localstorageban
   */
  public saveUser(userAuth: UserAuth): void {
    this.clean();
    localStorage.setItem(USER_NAME, userAuth.username);
    localStorage.setItem(USER_ROLE, userAuth.role);
    localStorage.setItem(USER_TOKEN, userAuth.token);
  }

  public saveCategory(id: number){
    this.cleanCategory();
    localStorage.setItem(CATEGORY_ID, id.toString());
  }

  /**
   * ha a localstorageban van ezzel a kulccsal valami, akkor visszaadja a felhasználó nevét
   * különben pedig null-t
   */
  public getUsername(): string | null {
    return localStorage.getItem(USER_NAME);
  }

  /**
   * ha a localstorageban van ezzel a kulccsal valami, akkor visszaadja a felhasználó jogosultságát
   * különben pedig null-t
   */
  public getUserRole(): string | null {
    return localStorage.getItem(USER_ROLE);
  }

  /**
   * ha a localstorageban van ezzel a kulccsal valami, akkor visszaadja a felhasználó tokenjét
   * különben pedig null-t
   */
  public getUserToken(): string | null {
    return localStorage.getItem(USER_TOKEN);
  }

  /**
   * ellenőrzi, hogy a felhasználó be van-e jelentkezve, úgyhogy a tárolót megnézi, hogy van-e benne token
   */
  public isLoggedIn(): boolean {
    const user = this.getUserToken();
    if (user) {
      return true;
    }
    return false;
  }

  getData(): StorageService {
    return this;
  }

  getCategory(): string | null {
    return localStorage.getItem(CATEGORY_ID);
  }

  getSearchWord(){
    return localStorage.getItem(SEARCH_WORD)
  }

  saveSearchWord(word: string) {
    this.cleanSeachWord()
    localStorage.setItem(SEARCH_WORD, word)
  }


  cleanSeachWord() {
    localStorage.removeItem(SEARCH_WORD)
  }
}
