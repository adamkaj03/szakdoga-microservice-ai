import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Book} from "../models/book";
import {StorageService} from "./storage.service";
import {BookRequest} from "../models/bookRequest";
import { environment } from 'src/environment';

/**
 * a könyvek lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class BookService {
  private readonly apiUrl = environment.apiUrl;
  //private apiUrl = "https://proba-spring.azurewebsites.net";
  readonly headers = new HttpHeaders({
    'Authorization': 'Bearer ' + this.storageService.getUserToken()
  });
  constructor(private http: HttpClient, private storageService: StorageService) { }

  /**
   * lekéri az összes könyvet egy get kérés segítségével a szervertől
   */
  public getBooks(): Observable<Book[]>{
    return this.http.get<Book[]>(this.apiUrl+"/api/books");
  }

  public getBook(title: string): Observable<Book>{
    let newTitle = title.replaceAll("/", "--")
    return this.http.get<Book>(this.apiUrl+ "/api/books/title/"+ newTitle);
  }

  getBooksByCategory(categoryId: number): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl+ "/api/books/category/"+ categoryId);
  }

  getBooksBySearchingWord(word: string): Observable<Book[]>{
    return this.http.get<Book[]>(this.apiUrl + "/api/books/search/" + word)
  }

  postBook(book: BookRequest, file: File | null) {
    const formData: FormData = new FormData()
    formData.append("bookRequest", JSON.stringify(book))
    formData.append("file", file!, file?.name)
    return this.http.post(this.apiUrl + "/api/books", formData, {headers: this.headers})
  }
}
