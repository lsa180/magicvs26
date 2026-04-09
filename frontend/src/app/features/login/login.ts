import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { isUsernameOrEmail } from '../../shared/validation';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  usernameOrEmail = '';
  password = '';
  message: string | null = null;
  error: string | null = null;
  loading = false;

  private readonly apiUrl = 'http://localhost:8080/api/users/login';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  onSubmit(): void {
    this.message = null;
    this.error = null;

    const usernameOrEmailTrimmed = this.usernameOrEmail.trim();
    const passwordTrimmed = this.password.trim();

    if (!isUsernameOrEmail(usernameOrEmailTrimmed)) {
      this.error = 'Introduce un nombre de usuario válido o un correo válido.';
      return;
    }

    if (!passwordTrimmed || passwordTrimmed.length < 8) {
      this.error = 'Introduce tu contraseña';
      return;
    }

    this.loading = true;

    this.http
      .post<UserResponse>(this.apiUrl, {
        usernameOrEmail: usernameOrEmailTrimmed,
        password: passwordTrimmed,
      })
      .subscribe({
        next: (user) => {
          this.loading = false;

          if (user.token) {
            localStorage.setItem('token', user.token);
          }

          localStorage.setItem('user', JSON.stringify(user));
          this.router.navigateByUrl('/');
        },
        error: (_err: HttpErrorResponse) => {
          // Usuario no existe o contraseña incorrecta (u otro error): mensaje genérico
          this.error = 'Credenciales incorrectas';
          this.loading = false;
        },
      });
  }

  onInputChange(): void {
    this.error = null;
    this.message = null;
  }
}

interface UserResponse {
  id: number;
  username: string;
  email: string;
  displayName: string | null;
  friendTag: string;
  token?: string;
  eloRating: number | null;
  friendsCount: number | null;
}