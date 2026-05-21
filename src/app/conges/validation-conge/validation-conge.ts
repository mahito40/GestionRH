import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CongeService } from '../../services/conge.service';
import { AuthService } from '../../services/auth.service';
import { Demande } from '../../models/conge.model';

@Component({
  selector: 'app-validation-conge',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './validation-conge.html',
})
export class ValidationCongeComponent {
  filtre = signal('en_attente');
  demandeSelectionnee = signal<Demande | null>(null);
  commentaire = '';

  typeLabels: Record<string, string> = {
    conge_annuel: 'Congé annuel', conge_maladie: 'Congé maladie',
    conge_maternite: 'Congé maternité', permission: 'Permission', autre: 'Autre'
  };

  constructor(public congeService: CongeService, public auth: AuthService) {}

  get demandes(): Demande[] {
    const all = this.congeService.demandes();
    if (!this.filtre()) return all;
    return all.filter(d => d.statut === this.filtre());
  }

  selectionner(d: Demande) { this.demandeSelectionnee.set(d); this.commentaire = ''; }
  fermer() { this.demandeSelectionnee.set(null); }

  valider(statut: 'approuve' | 'rejete') {
    const d = this.demandeSelectionnee();
    if (!d) return;
    const user = this.auth.currentUser()!;
    this.congeService.valider(d.id, statut, this.commentaire, `${user.prenom} ${user.nom}`);
    this.fermer();
  }
}
