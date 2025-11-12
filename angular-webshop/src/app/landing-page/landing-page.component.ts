import { Component, OnInit } from '@angular/core';
import { ChatService } from '../services/chat.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  public isChatOpen = false;

  constructor(private chatService: ChatService) {}

  ngOnInit(): void {
    this.chatService.isChatOpen$.subscribe(isOpen => {
      this.isChatOpen = isOpen;
    });
  }
}
