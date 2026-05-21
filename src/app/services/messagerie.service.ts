import { Injectable, signal } from '@angular/core';
import { Conversation, Message } from '../models/message.model';

@Injectable({ providedIn: 'root' })
export class MessagerieService {
  private _conversations = signal<Conversation[]>([
    { userId: 2, userName: 'Agossou Kofi', userRole: 'Manager', departement: 'Technique', dernierMessage: 'Bonjour, votre congé a été approuvé.', dateMessage: '09:30', nonLus: 1,
      messages: [
        { id: 1, expediteurId: 1, expediteurNom: 'Koudjo Ama', destinataireId: 2, contenu: 'Bonjour Chef, avez-vous reçu ma demande de congé ?', date: '09:15', lu: true },
        { id: 2, expediteurId: 2, expediteurNom: 'Agossou Kofi', destinataireId: 1, contenu: 'Bonjour, votre congé a été approuvé.', date: '09:30', lu: false },
      ]
    },
    { userId: 3, userName: 'Dossou Adjoa', userRole: 'RH', departement: 'RH', dernierMessage: "N'oubliez pas de soumettre vos documents.", dateMessage: 'Hier', nonLus: 0,
      messages: [
        { id: 3, expediteurId: 3, expediteurNom: 'Dossou Adjoa', destinataireId: 1, contenu: "Bonjour, n'oubliez pas de soumettre vos documents pour le congé.", date: 'Hier', lu: true },
      ]
    },
  ]);

  conversations = this._conversations.asReadonly();

  sendMessage(toUserId: number, contenu: string, fromUser: any): void {
    this._conversations.update(convs =>
      convs.map(c => {
        if (c.userId === toUserId) {
          const msg: Message = { id: Date.now(), expediteurId: fromUser.id, expediteurNom: `${fromUser.prenom} ${fromUser.nom}`, destinataireId: toUserId, contenu, date: new Date().toLocaleTimeString('fr', {hour:'2-digit', minute:'2-digit'}), lu: false };
          return { ...c, messages: [...c.messages, msg], dernierMessage: contenu, dateMessage: msg.date };
        }
        return c;
      })
    );
  }
}
