export interface Message {
  id: number;
  expediteurId: number;
  expediteurNom: string;
  destinataireId: number;
  contenu: string;
  date: string;
  lu: boolean;
}

export interface Conversation {
  userId: number;
  userName: string;
  userRole: string;
  departement: string;
  dernierMessage: string;
  dateMessage: string;
  nonLus: number;
  messages: Message[];
}
