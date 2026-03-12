import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { Job, JobRequest, JobSearchParams } from '../models/job.model';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = 'http://localhost:8080/api/jobs';

  constructor(private http: HttpClient) {}

  searchJobs(params: JobSearchParams): Observable<ApiResponse<Job[]>> {
    let httpParams = new HttpParams();
    if (params.title) httpParams = httpParams.set('title', params.title);
    if (params.location) httpParams = httpParams.set('location', params.location);
    if (params.experienceYears) httpParams = httpParams.set('experienceYears', params.experienceYears.toString());
    if (params.jobType) httpParams = httpParams.set('jobType', params.jobType);
    if (params.postedAfter) httpParams = httpParams.set('postedAfter', params.postedAfter);

    return this.http.get<ApiResponse<Job[]>>(`${this.apiUrl}/search`, { params: httpParams });
  }

  getActiveJobs(): Observable<ApiResponse<Job[]>> {
    return this.http.get<ApiResponse<Job[]>>(`${this.apiUrl}/active`);
  }

  getJobById(id: number): Observable<ApiResponse<Job>> {
    return this.http.get<ApiResponse<Job>>(`${this.apiUrl}/${id}`);
  }

  createJob(job: JobRequest): Observable<ApiResponse<Job>> {
    return this.http.post<ApiResponse<Job>>(this.apiUrl, job);
  }

  updateJob(id: number, job: JobRequest): Observable<ApiResponse<Job>> {
    return this.http.put<ApiResponse<Job>>(`${this.apiUrl}/${id}`, job);
  }

  deleteJob(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  toggleJobStatus(id: number): Observable<ApiResponse<Job>> {
    return this.http.patch<ApiResponse<Job>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  markJobAsFilled(id: number): Observable<ApiResponse<Job>> {
    return this.http.patch<ApiResponse<Job>>(`${this.apiUrl}/${id}/mark-filled`, {});
  }

  getMyJobs(): Observable<ApiResponse<Job[]>> {
    return this.http.get<ApiResponse<Job[]>>(`${this.apiUrl}/my-jobs`);
  }
}
