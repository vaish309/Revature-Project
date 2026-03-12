import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../models/api-response.model';
import { Application, ApplicationRequest, ApplicationStatus } from '../models/application.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = 'http://localhost:8080/api/applications';

  constructor(private http: HttpClient) {}

  // Backend only needs jobId — no resumeId, no coverLetter
  applyForJob(application: ApplicationRequest): Observable<ApiResponse<Application>> {
    return this.http.post<ApiResponse<Application>>(this.apiUrl, { jobId: application.jobId });
  }

  getApplicationDetails(applicationId: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/${applicationId}/details`);
  }

  // Backend endpoint is GET /my and returns plain List (not wrapped in ApiResponse)
  // We wrap it ourselves so the rest of the app works unchanged
  getMyApplications(): Observable<ApiResponse<Application[]>> {
    return this.http.get<Application[]>(`${this.apiUrl}/my`).pipe(
      map(data => ({ success: true, message: 'ok', data: data }))
    );
  }

  getJobApplications(jobId: number): Observable<ApiResponse<Application[]>> {
    return this.http.get<ApiResponse<Application[]>>(`${this.apiUrl}/job/${jobId}`);
  }

  updateApplicationStatus(id: number, status: ApplicationStatus, comments?: string): Observable<ApiResponse<Application>> {
    let params = new HttpParams().set('status', status);
    if (comments) {
      params = params.set('comments', comments);
    }
    return this.http.patch<ApiResponse<Application>>(`${this.apiUrl}/${id}/status`, {}, { params });
  }

  withdrawApplication(id: number, reason?: string): Observable<ApiResponse<Application>> {
    let params = new HttpParams();
    if (reason) {
      params = params.set('reason', reason);
    }
    return this.http.patch<ApiResponse<Application>>(`${this.apiUrl}/${id}/withdraw`, {}, { params });
  }
}
