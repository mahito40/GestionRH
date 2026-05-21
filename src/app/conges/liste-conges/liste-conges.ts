import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CongeService } from '../../services/conge.service';
import { Demande } from '../../models/conge.model';

@Component({
  selector: 'app-liste-conges',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './liste-conges.html',
})
export class ListeCongesComponent {
  filtreStatut = signal('');
  filtreType = signal('');

  constructor(public auth: AuthService, public congeService: CongeService) {}

  get demandes(): Demande[] {
    const user = this.auth.currentUser()!;
    let list = this.auth.hasRole('rh', 'admin')
      ? this.congeService.demandes()
      : this.congeService.getByEmploye(user.id);

    if (this.filtreStatut()) list = list.filter(d => d.statut === this.filtreStatut());
    if (this.filtreType()) list = list.filter(d => d.type === this.filtreType());
    return list;
  }

  typeLabel(t: string): Record<string, string> {
    return { conge_annuel: 'Congé annuel', conge_maladie: 'Congé maladie', conge_maternite: 'Congé maternité', permission: 'Permission', autre: 'Autre' };
  }

  getLabel(type: string): string {
    const map: Record<string, string> = { conge_annuel: 'Congé annuel', conge_maladie: 'Congé maladie', conge_maternite: 'Congé maternité', permission: 'Permission', autre: 'Autre' };
    return map[type] || type;
  }
}
