import { Component, EventEmitter, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { SearchType } from '../models/searchType.enum';

/**
 * Keresősáv komponens - felelős a különböző keresési módok kezeléséért
 * Támogatott keresési típusok: Normál, AI szöveges, AI képalapú
 */
@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {
  // Event emitterek a szülő komponens felé történő kommunikációhoz
  @Output() standardSearch = new EventEmitter<string>();
  @Output() aiTextSearch = new EventEmitter<string>();
  @Output() aiImageSearch = new EventEmitter<File>();

  // Aktív keresési mód
  activeSearchType: SearchType = SearchType.STANDARD;
  
  // Enum elérhetővé tétele a template számára
  SearchType = SearchType;
  
  // Képfeltöltéshez szükséges változók
  selectedImage: File | null = null;
  imagePreviewUrl: string | null = null;

  // Konstansok
  private readonly MAX_IMAGE_SIZE_MB = 5;
  private readonly MAX_IMAGE_SIZE_BYTES = this.MAX_IMAGE_SIZE_MB * 1024 * 1024;

  /**
   * Keresési form submit kezelése
   * A keresési típustól függően a megfelelő handlert hívja meg
   */
  onSubmit(searchForm: NgForm): void {
    switch (this.activeSearchType) {
      case SearchType.STANDARD:
        this.handleStandardSearch(searchForm);
        break;
      case SearchType.AI_TEXT:
        this.handleAITextSearch(searchForm);
        break;
      case SearchType.AI_IMAGE:
        this.handleAIImageSearch();
        break;
    }
  }

  /**
   * Normál szöveges keresés kezelése
   */
  private handleStandardSearch(searchForm: NgForm): void {
    const searchTerm = this.extractSearchTerm(searchForm);
    
    if (this.isValidSearchTerm(searchTerm)) {
      this.standardSearch.emit(searchTerm);
    }
  }

  /**
   * AI szöveges keresés kezelése (természetes nyelvi lekérdezések)
   */
  private handleAITextSearch(searchForm: NgForm): void {
    const searchQuery = this.extractSearchTerm(searchForm);
    
    if (this.isValidSearchTerm(searchQuery)) {
      this.aiTextSearch.emit(searchQuery);
    }
  }

  /**
   * AI képalapú keresés kezelése
   */
  private handleAIImageSearch(): void {
    if (this.selectedImage) {
      this.aiImageSearch.emit(this.selectedImage);
    }
  }

  /**
   * Keresési kifejezés kinyerése a form-ból
   */
  private extractSearchTerm(searchForm: NgForm): string {
    return searchForm.value.search?.trim() || '';
  }

  /**
   * Keresési kifejezés validálása
   */
  private isValidSearchTerm(term: string): boolean {
    return term !== null && term !== '';
  }

  /**
   * Keresési típus váltása
   * Képalapú keresésről váltáskor törli a kiválasztott képet
   */
  setSearchType(type: SearchType): void {
    this.activeSearchType = type;
    
    if (type !== SearchType.AI_IMAGE) {
      this.clearImageSelection();
    }
  }

  /**
   * Kép kiválasztásának kezelése
   * Validálja a fájl típusát és méretét
   */
  onImageSelected(event: Event): void {
    const file = this.extractFileFromEvent(event);
    
    if (!file) {
      return;
    }

    if (!this.validateImage(file)) {
      return;
    }

    this.selectedImage = file;
    this.createImagePreview(file);
  }

  /**
   * Fájl kinyerése az event-ből
   */
  private extractFileFromEvent(event: Event): File | null {
    const input = event.target as HTMLInputElement;
    return input.files?.[0] || null;
  }

  /**
   * Kép validálása (típus és méret)
   */
  private validateImage(file: File): boolean {
    if (!this.isValidImageType(file)) {
      this.showError('Kérjük, csak képfájlt töltsön fel!');
      return false;
    }

    if (!this.isValidImageSize(file)) {
      this.showError(`A kép mérete nem haladhatja meg a ${this.MAX_IMAGE_SIZE_MB}MB-ot!`);
      return false;
    }

    return true;
  }

  /**
   * Képfájl típus ellenőrzése
   */
  private isValidImageType(file: File): boolean {
    return file.type.startsWith('image/');
  }

  /**
   * Képfájl méret ellenőrzése
   */
  private isValidImageSize(file: File): boolean {
    return file.size <= this.MAX_IMAGE_SIZE_BYTES;
  }

  /**
   * Hibaüzenet megjelenítése
   */
  private showError(message: string): void {
    alert(message);
  }

  /**
   * Kép előnézet létrehozása
   */
  private createImagePreview(file: File): void {
    const reader = new FileReader();
    
    reader.onload = (e: ProgressEvent<FileReader>) => {
      this.imagePreviewUrl = e.target?.result as string;
    };
    
    reader.readAsDataURL(file);
  }

  /**
   * Kiválasztott kép törlése
   */
  clearImageSelection(): void {
    this.selectedImage = null;
    this.imagePreviewUrl = null;
  }

  /**
   * Dinamikus placeholder szöveg a keresési típus alapján
   */
  getSearchPlaceholder(): string {
    const placeholders: Record<SearchType, string> = {
      [SearchType.STANDARD]: 'Keresés könyvek között...',
      [SearchType.AI_TEXT]: 'Írja le, milyen könyvet keres... (pl. "izgalmas sci-fi regény")',
      [SearchType.AI_IMAGE]: 'Töltsön fel egy képet a könyvről...'
    };

    return placeholders[this.activeSearchType] || 'Keresés...';
  }

  /**
   * Dinamikus ikon a keresési típus alapján
   */
  getSearchIcon(): string {
    const icons: Record<SearchType, string> = {
      [SearchType.STANDARD]: 'bi-search',
      [SearchType.AI_TEXT]: 'bi-robot',
      [SearchType.AI_IMAGE]: 'bi-image'
    };

    return icons[this.activeSearchType] || 'bi-search';
  }
}
