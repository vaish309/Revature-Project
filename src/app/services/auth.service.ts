import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { LoginRequest, LoginResponse, RegisterJobSeekerRequest, RegisterEmployerRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  private getUserFromStorage(): LoginResponse | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  login(credentials: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            localStorage.setItem('token', response.data.token);
            this.currentUserSubject.next(response.data);
          }
        })
      );
  }

  registerJobSeeker(data: RegisterJobSeekerRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/register/jobseeker`, data)
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            localStorage.setItem('token', response.data.token);
            this.currentUserSubject.next(response.data);
          }
        })
      );
  }

  registerEmployer(data: RegisterEmployerRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.apiUrl}/register/employer`, data)
      .pipe(
        tap(response => {
          if (response.success && response.data) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            localStorage.setItem('token', response.data.token);
            this.currentUserSubject.next(response.data);
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  isJobSeeker(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'JOB_SEEKER';
  }

  isEmployer(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'EMPLOYER';
  }
}
