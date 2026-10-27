import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  message: string;
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly api = 'http://localhost:8080';

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api}/api/auth/login`, { username, password })
      .pipe(tap(response => localStorage.setItem('ems_token', response.token)));
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('ems_token');
  }

  logout(): void {
    localStorage.removeItem('ems_token');
  }

  token(): string | null {
    return localStorage.getItem('ems_token');
  }
}
