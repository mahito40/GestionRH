import { Injectable, signal } from '@angular/core';
import { Demande, StatutDemande, TypeDemande } from '../models/conge.model';

@Injectable({ providedIn: 'root' })
export class CongeService {
  private _demandes = signal<Demande[]>([
    { id: 1, employeId: 1, employeNom: 'Koudjo', employePrenom: 'Ama', departement: 'Technique', type: 'conge_annuel', dateDebut: '2024-02-01', dateFin: '2024-02-10', nbJours: 8, motif: 'Congé annuel planifié', statut: 'en_attente', dateDemande: '2024-01-20' },
    { id: 2, employeId: 1, employeNom: 'Koudjo', employePrenom: 'Ama', departement: 'Technique', type: 'permission', dateDebut: '2024-01-25', dateFin: '2024-01-25', nbJours: 1, motif: 'Rendez-vous médical', statut: 'approuve', dateDemande: '2024-01-18', commentaireValidateur: 'Approuvé', validateurNom: 'Agossou Kofi', dateValidation: '2024-01-19' },
    { id: 3, employeId: 3, employeNom: 'Dossou', employePrenom: 'Adjoa', departement: 'RH', type: 'conge_maladie', dateDebut: '2024-01-15', dateFin: '2024-01-17', nbJours: 3, motif: 'Maladie', statut: 'approuve', dateDemande: '2024-01-15', commentaireValidateur: 'Soignez-vous bien', validateurNom: 'Gbénou Komi', dateValidation: '2024-01-15' },
    { id: 4, employeId: 1, employeNom: 'Koudjo', employePrenom: 'Ama', departement: 'Technique', type: 'conge_annuel', dateDebut: '2024-03-10', dateFin: '2024-03-15', nbJours: 4, motif: 'Vacances familiales', statut: 'rejete', dateDemande: '2024-01-10', commentaireValidateur: 'Période chargée, reporter svp', validateurNom: 'Agossou Kofi', dateValidation: '2024-01-12' },
  ]);

  demandes = this._demandes.asReadonly();

  getByEmploye(employeId: number) {
    return this._demandes().filter(d => d.employeId === employeId);
  }

  getEnAttente() {
    return this._demandes().filter(d => d.statut === 'en_attente');
  }

  soumettre(data: Partial<Demande>): void {
    const newId = Math.max(...this._demandes().map(d => d.id)) + 1;
    const demande: Demande = {
      id: newId,
      employeId: data.employeId!,
      employeNom: data.employeNom!,
      employePrenom: data.employePrenom!,
      departement: data.departement!,
      type: data.type as TypeDemande,
      dateDebut: data.dateDebut!,
      dateFin: data.dateFin!,
      nbJours: data.nbJours!,
      motif: data.motif!,
      statut: 'en_attente',
      dateDemande: new Date().toISOString().split('T')[0],
    };
    this._demandes.update(d => [...d, demande]);
  }

  valider(id: number, statut: 'approuve' | 'rejete', commentaire: string, validateurNom: string): void {
    this._demandes.update(demandes =>
      demandes.map(d => d.id === id ? {
        ...d, statut, commentaireValidateur: commentaire,
        validateurNom, dateValidation: new Date().toISOString().split('T')[0]
      } : d)
    );
  }
}
