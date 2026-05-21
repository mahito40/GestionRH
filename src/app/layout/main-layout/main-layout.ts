import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar';
import { NavbarComponent } from '../navbar/navbar';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, SidebarComponent, NavbarComponent],
  template: `
    <app-sidebar [isOpen]="sidebarOpen()" (closed)="sidebarOpen.set(false)" />
    <div class="main-wrapper">
      <app-navbar (toggleSidebar)="sidebarOpen.set(!sidebarOpen())" />
      <main class="page-content">
        <router-outlet />
      </main>
    </div>
  `
})
export class MainLayoutComponent {
  sidebarOpen = signal(false);
}
