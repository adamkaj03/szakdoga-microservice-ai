import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatMessage } from '../models/chatMessage';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private isChatOpenSubject = new BehaviorSubject<boolean>(false);
  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  
  public isChatOpen$: Observable<boolean> = this.isChatOpenSubject.asObservable();
  public messages$: Observable<ChatMessage[]> = this.messagesSubject.asObservable();

  constructor() {
    // TODO: Replace with real API call when endpoint is available
    this.initializeDummyMessages();
  }

  private initializeDummyMessages(): void {
    const dummyMessages: ChatMessage[] = [
      {
        id: 1,
        text: 'Szia! Miben segíthetek?',
        sender: 'ai',
        timestamp: new Date(Date.now() - 3600000) // 1 hour ago
      },
      {
        id: 2,
        text: 'Keresek egy jó sci-fi könyvet.',
        sender: 'user',
        timestamp: new Date(Date.now() - 3500000)
      },
      {
        id: 3,
        text: 'Remek választás! Ajánlom a "Dűne" című könyvet Frank Herbert-től. Ez egy klasszikus sci-fi mű, amely egy messzi jövőben játszódik. A történet a sivatagi Arrakis bolygóról szól, ahol az értékes fűszer található.',
        sender: 'ai',
        timestamp: new Date(Date.now() - 3400000)
      },
      {
        id: 4,
        text: 'Érdekes! Van még más ajánlatod?',
        sender: 'user',
        timestamp: new Date(Date.now() - 3300000)
      },
      {
        id: 5,
        text: 'Természetesen! Ha modern sci-fit keresel, akkor a "Ready Player One" Ernest Cline-tól nagyszerű választás. Vagy ha cyberpunk hangulatot szeretnél, akkor William Gibson "Neurománc" című műve tökéletes.',
        sender: 'ai',
        timestamp: new Date(Date.now() - 3200000)
      }
    ];
    
    this.messagesSubject.next(dummyMessages);
  }

  public toggleChat(): void {
    this.isChatOpenSubject.next(!this.isChatOpenSubject.value);
  }

  public openChat(): void {
    this.isChatOpenSubject.next(true);
  }

  public closeChat(): void {
    this.isChatOpenSubject.next(false);
  }

  public sendMessage(text: string): void {
    const currentMessages = this.messagesSubject.value;
    const userMessage: ChatMessage = {
      id: currentMessages.length + 1,
      text: text,
      sender: 'user',
      timestamp: new Date()
    };
    
    this.messagesSubject.next([...currentMessages, userMessage]);
    
    // TODO: Replace with real API call when endpoint is available
    // Simulate AI response
    setTimeout(() => {
      const aiResponse: ChatMessage = {
        id: currentMessages.length + 2,
        text: 'Köszönöm az üzeneted! Jelenleg dummy válaszban vagyunk. Hamarosan valódi AI válaszokat fogsz kapni!',
        sender: 'ai',
        timestamp: new Date()
      };
      this.messagesSubject.next([...this.messagesSubject.value, aiResponse]);
    }, 1000);
  }

  public getMessages(): ChatMessage[] {
    return this.messagesSubject.value;
  }

  public isChatOpen(): boolean {
    return this.isChatOpenSubject.value;
  }
}
