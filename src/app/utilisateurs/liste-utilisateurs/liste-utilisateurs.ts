import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-liste-utilisateurs',
  imports: [CommonModule, FormsModule],
  templateUrl: './liste-utilisateurs.html',
})
export class ListeUtilisateursComponent {
  recherche = '';
  utilisateurs: User[] = [
    { id: 1, nom: 'Koudjo', prenom: 'Ama', email: 'ama.koudjo@homintec.com', role: 'employe', departement: 'Technique', poste: 'Ingénieur', soldeConges: 18, soldePermissions: 3 },
    { id: 2, nom: 'Agossou', prenom: 'Kofi', email: 'kofi.agossou@homintec.com', role: 'manager', departement: 'Technique', poste: 'Chef de projet', soldeConges: 20, soldePermissions: 5 },
    { id: 3, nom: 'Dossou', prenom: 'Adjoa', email: 'adjoa.dossou@homintec.com', role: 'rh', departement: 'RH', poste: 'Responsable RH', soldeConges: 20, soldePermissions: 5 },
    { id: 4, nom: 'Gbénou', prenom: 'Komi', email: 'komi.gbenou@homintec.com', role: 'admin', departement: 'Direction', poste: 'Directeur Général', soldeConges: 25, soldePermissions: 10 },
  ];

  get filtres(): User[] {
    if (!this.recherche) return this.utilisateurs;
    const q = this.recherche.toLowerCase();
    return this.utilisateurs.filter(u =>
      u.nom.toLowerCase().includes(q) || u.prenom.toLowerCase().includes(q) ||
      u.email.toLowerCase().includes(q) || u.departement.toLowerCase().includes(q)
    );
  }

  roleColor(role: string): string {
    return { admin: '#7c3aed', rh: '#0891b2', manager: '#059669', employe: '#2563a8' }[role] || '#64748b';
  }
}
