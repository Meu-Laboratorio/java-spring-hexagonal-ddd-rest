import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../core/services/user-service';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.html',
  styleUrl: './user-form.css',
})
export class UserForm {
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  
  form = this.fb.nonNullable.group({
    name: ['', Validators.required]
  });

  accessForm = this.fb.nonNullable.group({
    userId: [null as number | null, [Validators.required, Validators.min(1)]]
  });

  isLoading = signal(false);
  isAccessLoading = signal(false);
  successMessage = signal('');
  errorMessage = signal('');

  onSubmit() {
    if (this.form.valid) {
      this.isLoading.set(true);
      this.successMessage.set('');
      this.errorMessage.set('');
      
      const name = this.form.controls.name.value;
      this.userService.createUser({ name }).subscribe({
        next: (user) => {
          console.log('Usuário criado:', user);
          this.successMessage.set('Usuário criado com sucesso!');
          setTimeout(() => {
            this.router.navigate(['/users', user.id]);
          }, 500);
        },
        error: (error) => {
          console.error('Erro ao criar usuário:', error);
          this.errorMessage.set('Erro ao criar usuário. Tente novamente.');
          this.isLoading.set(false);
        }
      });
    }
  }

  onAccessUser() {
    if (this.accessForm.valid) {
      this.isAccessLoading.set(true);
      this.errorMessage.set('');
      this.successMessage.set('');
      
      const userId = this.accessForm.controls.userId.value;
      if (userId) {
        this.userService.getUser(userId).subscribe({
          next: (user) => {
            console.log('Usuário encontrado:', user);
            this.router.navigate(['/users', user.id]);
          },
          error: (error) => {
            console.error('Erro ao buscar usuário:', error);
            this.errorMessage.set(`Usuário com ID ${userId} não encontrado.`);
            this.isAccessLoading.set(false);
          }
        });
      }
    }
  }
}
