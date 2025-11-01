import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {Order} from "../models/order";
import {StorageService} from "./storage.service";
import {Cart} from "../models/cart";
import {ShippingType} from "../models/shippingType";
import { ArchiveLog } from "../models/archiveLog";


/**
 * a rendelések lekérését végző service
 */
@Injectable({
  providedIn: 'root'
})
export class ArchiveService {
  private apiUrl = "http://localhost:8080";


   private headers = new HttpHeaders({
    'Authorization': 'Bearer ' + this.storageService.getUserToken() 
  });

  constructor(private http: HttpClient, private storageService: StorageService) { }

  public getArchiveLogs(): Observable<ArchiveLog[]>{
    return this.http.get<ArchiveLog[]>(this.apiUrl+"/api/archive/logs",  { headers: this.headers });
  }

  public createArchive(selectedYear: number | null) {
    if (selectedYear === null) {
        throw new Error('Selected year is required');
    }

    return this.http.post<void>(`${this.apiUrl}/api/archive`, null, {
        params: { year: selectedYear.toString() },
        headers: this.headers
    });          
  }
}
