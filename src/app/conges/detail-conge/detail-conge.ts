import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CongeService } from '../../services/conge.service';
import { AuthService } from '../../services/auth.service';
import { Demande } from '../../models/conge.model';

@Component({
  selector: 'app-detail-conge',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './detail-conge.html',
})
export class DetailCongeComponent implements OnInit {
  demande = signal<Demande | null>(null);
  commentaire = '';
  actionDone = signal(false);

  typeLabels: Record<string, string> = {
    conge_annuel: 'Congé annuel', conge_maladie: 'Congé maladie',
    conge_maternite: 'Congé maternité', permission: 'Permission', autre: 'Autre'
  };

  constructor(
    private route: ActivatedRoute,
    public congeService: CongeService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!);
    const found = this.congeService.demandes().find(d => d.id === id);
    this.demande.set(found || null);
  }

  valider(statut: 'approuve' | 'rejete') {
    const d = this.demande();
    if (!d) return;
    const user = this.auth.currentUser()!;
    this.congeService.valider(d.id, statut, this.commentaire, `${user.prenom} ${user.nom}`);
    const updated = this.congeService.demandes().find(x => x.id === d.id);
    this.demande.set(updated || null);
    this.actionDone.set(true);
  }

  canValidate(): boolean {
    return this.demande()?.statut === 'en_attente' && this.auth.hasRole('manager', 'rh', 'admin');
  }
}
