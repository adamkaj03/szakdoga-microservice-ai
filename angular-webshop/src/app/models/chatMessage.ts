import { ChatSender } from "./chatSender.enum";

export interface ChatMessage {
  text: string;
  sender: ChatSender;
  timestamp: Date;
  dateTime?: string;
}
