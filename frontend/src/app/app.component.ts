import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Message {
  id: number;
  firstText: string;
  secondText: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  messageList:Message[] = [];
  constructor(private http: HttpClient) {
    http.get<Message[]>('http://localhost:8080/v1/message-list').subscribe(data => this.messageList = data);
  }
}
