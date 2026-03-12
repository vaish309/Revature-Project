import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { Resume, Education, Experience, Skill, Certification } from '../models/resume.model';
import { Job } from '../models/job.model';

@Injectable({
  providedIn: 'root'
})
export class JobSeekerService {
  private apiUrl = 'http://localhost:8080/api/jobseeker';

  constructor(private http: HttpClient) {}

  // Resume
  createOrUpdateResume(objective: string): Observable<ApiResponse<Resume>> {
    return this.http.post<ApiResponse<Resume>>(`${this.apiUrl}/resume`, { objective });
  }

  uploadResume(file: File): Observable<ApiResponse<Resume>> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<ApiResponse<Resume>>(`${this.apiUrl}/resume/upload`, formData);
  }

  getResume(): Observable<ApiResponse<Resume>> {
    return this.http.get<ApiResponse<Resume>>(`${this.apiUrl}/resume`);
  }

  // Education
  addEducation(education: Partial<Education>): Observable<ApiResponse<Education>> {
    return this.http.post<ApiResponse<Education>>(`${this.apiUrl}/education`, education);
  }

  getEducation(): Observable<ApiResponse<Education[]>> {
    return this.http.get<ApiResponse<Education[]>>(`${this.apiUrl}/education`);
  }

  deleteEducation(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/education/${id}`);
  }

  // Experience
  addExperience(experience: Partial<Experience>): Observable<ApiResponse<Experience>> {
    return this.http.post<ApiResponse<Experience>>(`${this.apiUrl}/experience`, experience);
  }

  getExperience(): Observable<ApiResponse<Experience[]>> {
    return this.http.get<ApiResponse<Experience[]>>(`${this.apiUrl}/experience`);
  }

  deleteExperience(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/experience/${id}`);
  }

  // Skills
  addSkill(skill: Partial<Skill>): Observable<ApiResponse<Skill>> {
    return this.http.post<ApiResponse<Skill>>(`${this.apiUrl}/skills`, skill);
  }

  getSkills(): Observable<ApiResponse<Skill[]>> {
    return this.http.get<ApiResponse<Skill[]>>(`${this.apiUrl}/skills`);
  }

  deleteSkill(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/skills/${id}`);
  }

  // Certifications
  addCertification(certification: Partial<Certification>): Observable<ApiResponse<Certification>> {
    return this.http.post<ApiResponse<Certification>>(`${this.apiUrl}/certifications`, certification);
  }

  getCertifications(): Observable<ApiResponse<Certification[]>> {
    return this.http.get<ApiResponse<Certification[]>>(`${this.apiUrl}/certifications`);
  }

  deleteCertification(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/certifications/${id}`);
  }

  // Saved Jobs
  saveJob(jobId: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/saved-jobs/${jobId}`, {});
  }

  unsaveJob(jobId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/saved-jobs/${jobId}`);
  }

  getSavedJobs(): Observable<ApiResponse<Job[]>> {
    return this.http.get<ApiResponse<Job[]>>(`${this.apiUrl}/saved-jobs`);
  }
}
