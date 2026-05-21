import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profil',
  imports: [CommonModule, FormsModule],
  templateUrl: './profil.html',
})
export class ProfilComponent {
  editMode = signal(false);
  successMsg = signal('');

  constructor(public auth: AuthService) {}

  save() {
    this.editMode.set(false);
    this.successMsg.set('Profil mis à jour avec succès.');
    setTimeout(() => this.successMsg.set(''), 3000);
  }
}
