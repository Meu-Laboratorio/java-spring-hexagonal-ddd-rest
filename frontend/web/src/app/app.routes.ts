import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: 'users/new', loadComponent: () => import('./features/users/user-form/user-form').then(m => m.UserForm) },
    { path: 'users/:userId', loadComponent: () => import('./features/users/user-detail/user-detail').then(m => m.UserDetail) },
    { path: '', redirectTo: 'users/new', pathMatch: 'full' },
    { path: '**', redirectTo: 'users/new' }
];
