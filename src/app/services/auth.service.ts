import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private _currentUser = signal<User | null>(null);
  currentUser = this._currentUser.asReadonly();

  private mockUsers: User[] = [
    { id: 1, nom: 'Koudjo', prenom: 'Ama', email: 'ama.koudjo@homintec.com', role: 'employe', departement: 'Technique', poste: 'Ingénieur', soldeConges: 18, soldePermissions: 3 },
    { id: 2, nom: 'Agossou', prenom: 'Kofi', email: 'kofi.agossou@homintec.com', role: 'manager', departement: 'Technique', poste: 'Chef de projet', soldeConges: 20, soldePermissions: 5 },
    { id: 3, nom: 'Dossou', prenom: 'Adjoa', email: 'adjoa.dossou@homintec.com', role: 'rh', departement: 'RH', poste: 'Responsable RH', soldeConges: 20, soldePermissions: 5 },
    { id: 4, nom: 'Gbénou', prenom: 'Komi', email: 'komi.gbenou@homintec.com', role: 'admin', departement: 'Direction', poste: 'Directeur Général', soldeConges: 25, soldePermissions: 10 },
  ];

  constructor(private router: Router) {
    const stored = localStorage.getItem('homintec_user');
    if (stored) this._currentUser.set(JSON.parse(stored));
  }

  login(email: string, password: string): boolean {
    const user = this.mockUsers.find(u => u.email === email);
    if (user && password === 'homintec2024') {
      this._currentUser.set(user);
      localStorage.setItem('homintec_user', JSON.stringify(user));
      return true;
    }
    return false;
  }

  logout() {
    this._currentUser.set(null);
    localStorage.removeItem('homintec_user');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean { return !!this._currentUser(); }

  hasRole(...roles: string[]): boolean {
    const u = this._currentUser();
    return !!u && roles.includes(u.role);
  }
}
