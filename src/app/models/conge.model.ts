export type StatutDemande = 'en_attente' | 'approuve' | 'rejete';
export type TypeDemande = 'conge_annuel' | 'conge_maladie' | 'conge_maternite' | 'permission' | 'autre';

export interface Demande {
  id: number;
  employeId: number;
  employeNom: string;
  employePrenom: string;
  departement: string;
  type: TypeDemande;
  dateDebut: string;
  dateFin: string;
  nbJours: number;
  motif: string;
  statut: StatutDemande;
  dateDemande: string;
  commentaireValidateur?: string;
  validateurId?: number;
  validateurNom?: string;
  dateValidation?: string;
}
