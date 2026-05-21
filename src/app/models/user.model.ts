export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  role: 'employe' | 'manager' | 'rh' | 'admin';
  departement: string;
  poste: string;
  avatar?: string;
  soldeConges: number;
  soldePermissions: number;
}
