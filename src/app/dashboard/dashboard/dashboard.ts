import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CongeService } from '../../services/conge.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
})
export class DashboardComponent {
  constructor(public auth: AuthService, public congeService: CongeService) {}

  get mesStats() {
    const user = this.auth.currentUser()!;
    const mesDemandes = this.congeService.getByEmploye(user.id);
    return {
      soldeConges: user.soldeConges,
      soldePermissions: user.soldePermissions,
      enAttente: mesDemandes.filter(d => d.statut === 'en_attente').length,
      approuvees: mesDemandes.filter(d => d.statut === 'approuve').length,
    };
  }

  get demandesRecentes() {
    const user = this.auth.currentUser()!;
    return this.congeService.getByEmploye(user.id).slice(0, 4);
  }

  get demandesAValider() {
    return this.congeService.getEnAttente();
  }

  getTypeLabel(type: string): string {
    const labels: Record<string, string> = {
      conge_annuel: 'Congé annuel', conge_maladie: 'Congé maladie',
      conge_maternite: 'Congé maternité', permission: 'Permission', autre: 'Autre'
    };
    return labels[type] || type;
  }
}
