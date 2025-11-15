import { Component, OnInit, HostListener } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-chat-button',
  templateUrl: './chat-button.component.html',
  styleUrls: ['./chat-button.component.css']
})
export class ChatButtonComponent implements OnInit {
  public isChatOpen = false;
  public isLoggedIn = false;
  public bottomPosition = 20; // Default bottom position in pixels
  private footerHeight = 200; // Approximate footer height

  constructor(
    private chatService: ChatService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.chatService.isChatOpen$.subscribe(isOpen => {
      this.isChatOpen = isOpen;
    });
    this.isLoggedIn = this.authService.isLoggedIn();
    this.updateButtonPosition();
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    this.updateButtonPosition();
  }

  private updateButtonPosition(): void {
    const windowHeight = window.innerHeight;
    const documentHeight = document.documentElement.scrollHeight;
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    
    // Calculate distance from bottom
    const distanceFromBottom = documentHeight - (scrollTop + windowHeight);
    
    // If we're close to the footer, move the button up
    if (distanceFromBottom < this.footerHeight) {
      this.bottomPosition = this.footerHeight - distanceFromBottom + 20;
    } else {
      this.bottomPosition = 20;
    }
  }

  public toggleChat(): void {
    this.chatService.toggleChat();
  }
}
