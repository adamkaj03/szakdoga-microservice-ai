import { Component } from '@angular/core';
import { StorageService } from "../services/storage.service";
import { Router } from "@angular/router";
import { CategoryService } from "../services/category.service";
import { Observable } from "rxjs";
import { Category } from "../models/category";

/**
 * Navigációs sáv komponens
 * Felelősségei: navigáció, kategóriák megjelenítése, keresés koordinálása
 */
@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {

  public storageServiceData: StorageService;
  public categories: Observable<Category[]> = new Observable<Category[]>();
  
  constructor(
    private readonly router: Router,
    private readonly storageService: StorageService,
    private readonly categoryService: CategoryService)
  {
    this.storageServiceData = this.storageService.getData();
    this.categories = this.categoryService.getCategories();
  }

  /**
   * Bejelentkezési oldalra navigálás
   */
  navigateToSignIn(): void {
    this.router.navigateByUrl("/signin");
  }

  /**
   * Főoldalra navigálás - törli a szűrőket
   */
  navigateToHome(event: Event): void {
    event.preventDefault();
    this.clearAllFilters();
    this.reloadPage();
  }

  /**
   * Ellenőrzi, hogy a felhasználó admin-e
   */
  isUserAdmin(): boolean {
    const userRole = this.storageServiceData.getUserRole();
    return userRole !== null && userRole === "ADMIN";
  }

  /**
   * Ellenőrzi, hogy a felhasználó be van-e jelentkezve
   */
  isUserSignedIn(): boolean {
    return this.storageServiceData.getUsername() !== null;
  }

  /**
   * Kategória alapú szűrés - újratölti az oldalt a kiválasztott kategóriával
   */
  refreshWithCategory(id: number | null): void {
    this.storageService.cleanSeachWord();
    
    if (id === null) {
      this.storageService.cleanCategory();
    } else {
      this.storageService.saveCategory(id);
    }
    
    this.reloadPage();
  }

  /**
   * Kosár oldalra navigálás
   */
  navigateToCart(): void {
    this.router.navigateByUrl("/cart");
  }

  /**
   * Normál keresés kezelése
   * A search-bar komponens által kibocsátott esemény kezelője
   */
  onStandardSearch(searchTerm: string): void {
    this.storageService.cleanCategory();
    this.storageService.saveSearchWord(searchTerm);
    this.navigateToSearchResults();
  }

  /**
   * AI szöveges keresés kezelése
   * A search-bar komponens által kibocsátott esemény kezelője
   */
  onAITextSearch(query: string): void {
    // TODO: AI szöveges keresés szolgáltatás integrálása
    console.log('AI szöveges keresés:', query);
    // this.aiSearchService.searchByText(query).subscribe(results => {
    //   this.handleSearchResults(results);
    // });
  }

  /**
   * AI képalapú keresés kezelése
   * A search-bar komponens által kibocsátott esemény kezelője
   */
  onAIImageSearch(image: File): void {
    // TODO: AI képalapú keresés szolgáltatás integrálása
    console.log('AI képalapú keresés:', image.name, image.size);
    // this.aiSearchService.searchByImage(image).subscribe(results => {
    //   this.handleSearchResults(results);
    // });
  }

  /**
   * Keresési eredmények oldalra navigálás és újratöltés
   */
  private navigateToSearchResults(): void {
    this.router.navigateByUrl("/search").then(() => {
      this.reloadPage();
    });
  }

  /**
   * Összes szűrő törlése
   */
  private clearAllFilters(): void {
    this.storageService.cleanCategory();
    this.storageService.cleanSeachWord();
  }

  /**
   * Oldal újratöltése
   */
  private reloadPage(): void {
    globalThis.location.reload();
  }
}
