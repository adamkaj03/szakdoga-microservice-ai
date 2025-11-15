import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatMessage } from '../models/chatMessage';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from './storage.service';
import { ChatSender } from '../models/chatSender.enum';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly isChatOpenSubject = new BehaviorSubject<boolean>(false);
  private readonly messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  private readonly apiUrl = 'http://localhost:8080/api/ai-chat';
  private messagesLoaded = false;
  
  public isChatOpen$: Observable<boolean> = this.isChatOpenSubject.asObservable();
  public messages$: Observable<ChatMessage[]> = this.messagesSubject.asObservable();

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': 'Bearer ' + this.storageService.getUserToken()
    });
  }

  private loadMessagesFromApi(): void {
    const username = this.storageService.getUsername();
    
    if (!username) {
      console.error('Username not found');
      return;
    }

    this.http.get<ChatMessage[]>(
      `${this.apiUrl}/messages?username=${username}`,
      { headers: this.getHeaders() }
    ).subscribe({
      next: (messages) => {
        // Konvertáljuk a dateTime stringet Date objektummá
        const convertedMessages = messages.map(msg => ({
          ...msg,
          timestamp: msg.dateTime ? new Date(msg.dateTime) : new Date()
        }));
        this.messagesSubject.next(convertedMessages);
        this.messagesLoaded = true;
      },
      error: (error) => {
        console.error('Error loading messages:', error);
      }
    });
  }

  public toggleChat(): void {
    const newState = !this.isChatOpenSubject.value;
    this.isChatOpenSubject.next(newState);
    
    // Ha most nyílik meg a chat és még nem töltöttük be az üzeneteket
    if (newState && !this.messagesLoaded) {
      this.loadMessagesFromApi();
    }
  }

  public openChat(): void {
    this.isChatOpenSubject.next(true);
    
    // Ha még nem töltöttük be az üzeneteket
    if (!this.messagesLoaded) {
      this.loadMessagesFromApi();
    }
  }

  public closeChat(): void {
    this.isChatOpenSubject.next(false);
  }

  public sendMessage(text: string): void {
    const currentMessages = this.messagesSubject.value;
    const now = new Date();
    const userMessage: ChatMessage = {
      text: text,
      sender: ChatSender.USER,
      timestamp: now
    };
    
    // Azonnal hozzáadjuk a user üzenetét a listához
    this.messagesSubject.next([...currentMessages, userMessage]);
    
    const username = this.storageService.getUsername();
    if (!username) {
      console.error('Username not found');
      return;
    }

    // Formázzuk a dátumot LocalDateTime formátumra (ISO 8601 without 'Z')
    const dateTimeString = now.toISOString().slice(0, 19);

    // Request body a backend számára
    const requestBody = {
      sender: ChatSender.USER,
      text: text,
      dateTime: dateTimeString
    };

    // API hívás a backend-nek
    this.http.post<ChatMessage>(
      `${this.apiUrl}/send-message?username=${username}`,
      requestBody,
      { headers: this.getHeaders() }
    ).subscribe({
      next: (aiResponse) => {
        // Konvertáljuk a dateTime-ot timestamp-re
        const convertedResponse: ChatMessage = {
          ...aiResponse,
          timestamp: aiResponse.dateTime ? new Date(aiResponse.dateTime) : new Date()
        };
        // Hozzáadjuk az AI választ a listához
        this.messagesSubject.next([...this.messagesSubject.value, convertedResponse]);
      },
      error: (error) => {
        console.error('Error sending message:', error);
        // Hibakezelés: fallback üzenet
        const errorMessage: ChatMessage = {
          text: 'Sajnálom, jelenleg nem tudok válaszolni. Kérlek próbáld újra később!',
          sender: ChatSender.AI,
          timestamp: new Date()
        };
        this.messagesSubject.next([...this.messagesSubject.value, errorMessage]);
      }
    });
  }

  public getMessages(): ChatMessage[] {
    return this.messagesSubject.value;
  }

  public isChatOpen(): boolean {
    return this.isChatOpenSubject.value;
  }
}
