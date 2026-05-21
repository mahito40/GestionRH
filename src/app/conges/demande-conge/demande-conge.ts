import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CongeService } from '../../services/conge.service';

@Component({
  selector: 'app-demande-conge',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './demande-conge.html',
})
export class DemandeCongeComponent {
  form = { type: '', dateDebut: '', dateFin: '', motif: '', justificatif: '' };
  submitted = signal(false);
  success = signal(false);
  error = signal('');

  types = [
    { value: 'conge_annuel', label: 'Congé annuel' },
    { value: 'conge_maladie', label: 'Congé maladie' },
    { value: 'conge_maternite', label: 'Congé maternité/paternité' },
    { value: 'permission', label: 'Permission' },
    { value: 'autre', label: 'Autre' },
  ];

  constructor(public auth: AuthService, public congeService: CongeService, private router: Router) {}

  get nbJours(): number {
    if (!this.form.dateDebut || !this.form.dateFin) return 0;
    const d1 = new Date(this.form.dateDebut);
    const d2 = new Date(this.form.dateFin);
    const diff = Math.ceil((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    return Math.max(0, diff);
  }

  get todayStr(): string { return new Date().toISOString().split('T')[0]; }

  onSubmit() {
    this.error.set('');
    if (!this.form.type || !this.form.dateDebut || !this.form.dateFin || !this.form.motif) {
      this.error.set('Veuillez remplir tous les champs obligatoires.'); return;
    }
    if (this.nbJours <= 0) { this.error.set('La date de fin doit être après la date de début.'); return; }
    const user = this.auth.currentUser()!;
    this.congeService.soumettre({
      employeId: user.id, employeNom: user.nom, employePrenom: user.prenom,
      departement: user.departement, type: this.form.type as any,
      dateDebut: this.form.dateDebut, dateFin: this.form.dateFin,
      nbJours: this.nbJours, motif: this.form.motif,
    });
    this.success.set(true);
    setTimeout(() => this.router.navigate(['/conges/mes-demandes']), 2000);
  }
}
