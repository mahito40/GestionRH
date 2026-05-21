import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { MessagerieService } from '../../services/messagerie.service';
import { Conversation } from '../../models/message.model';

@Component({
  selector: 'app-messagerie',
  imports: [CommonModule, FormsModule],
  templateUrl: './messagerie.html',
})
export class MessagerieComponent {
  convActive = signal<Conversation | null>(null);
  nouveauMessage = '';

  constructor(public auth: AuthService, public msgService: MessagerieService) {}

  selectionner(conv: Conversation) {
    this.convActive.set(conv);
    this.nouveauMessage = '';
  }

  envoyer() {
    const conv = this.convActive();
    if (!conv || !this.nouveauMessage.trim()) return;
    this.msgService.sendMessage(conv.userId, this.nouveauMessage.trim(), this.auth.currentUser());
    const updated = this.msgService.conversations().find(c => c.userId === conv.userId);
    this.convActive.set(updated || null);
    this.nouveauMessage = '';
  }

  get currentUserId(): number {
    return this.auth.currentUser()?.id ?? 0;
  }
}
