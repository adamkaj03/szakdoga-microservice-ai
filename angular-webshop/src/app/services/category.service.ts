import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "../models/category";
import {StorageService} from "./storage.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

/**
 * a kategóriák lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = "http://localhost:8080";
  //private apiUrl = "https://proba-spring.azurewebsites.net";

  readonly headers = new HttpHeaders({
    'Authorization': 'Bearer ' + this.storageService.getUserToken()
  });

  constructor(private http: HttpClient, private storageService: StorageService) { }

  /**
   * lekéri az összes kategóriát egy get kérés segítségével a szervertől
   */
  public getCategories(): Observable<Category[]>{
    return this.http.get<Category[]>(this.apiUrl+"/api/kategoriak");
  }


  public postCategory(category: string) {
    return this.http.post<Category>(this.apiUrl+"/api/kategoriak", '' + category + '', { headers: this.headers })
  }

    getCategoryById(id: number) {
        return this.http.get<Category>(this.apiUrl+"/api/kategoriak/" + id);
    }
}
