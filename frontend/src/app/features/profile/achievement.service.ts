import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AchievementDto {
  code: string;
  name: string;
  description?: string | null;
  badgeUri?: string | null;
  threshold?: number | null;
  trigger?: string | null;
}

@Injectable({ providedIn: 'root' })
export class AchievementService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/achievements';

  listAll(): Observable<AchievementDto[]> {
    return this.http.get<AchievementDto[]>(this.apiUrl);
  }

  myAchievements(): Observable<AchievementDto[]> {
    return this.http.get<AchievementDto[]>(`${this.apiUrl}/me`, { headers: this.authHeaders() });
  }

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }
}
