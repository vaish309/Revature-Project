import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { JobService } from '../../services/job.service';
import { JobRequest, JobType } from '../../models/job.model';

@Component({
  selector: 'app-create-job',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="padding: 40px 0;">
      <h1>Post a New Job</h1>
      
      @if (errorMessage) {
        <div class="alert alert-error">{{ errorMessage }}</div>
      }
      
      <div class="card">
        <form (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label for="title">Job Title *</label>
            <input type="text" id="title" class="form-control" [(ngModel)]="formData.title" name="title" required />
          </div>
          
          <div class="form-group">
            <label for="description">Job Description *</label>
            <textarea id="description" class="form-control" [(ngModel)]="formData.description" name="description" rows="6" required></textarea>
          </div>
          
          <div class="form-group">
            <label for="requiredSkills">Required Skills *</label>
            <input type="text" id="requiredSkills" class="form-control" [(ngModel)]="formData.requiredSkills" name="requiredSkills" placeholder="e.g. Java, Spring Boot, MySQL" required />
          </div>
          
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
            <div class="form-group">
              <label for="location">Location *</label>
              <input type="text" id="location" class="form-control" [(ngModel)]="formData.location" name="location" required />
            </div>
            
            <div class="form-group">
              <label for="salaryRange">Salary Range *</label>
              <input type="text" id="salaryRange" class="form-control" [(ngModel)]="formData.salaryRange" name="salaryRange" placeholder="e.g. $80,000 - $120,000" required />
            </div>
          </div>
          
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
            <div class="form-group">
              <label for="jobType">Job Type *</label>
              <select id="jobType" class="form-control" [(ngModel)]="formData.jobType" name="jobType" required>
                @for (type of jobTypes; track type) {
                  <option [value]="type">{{ type.replace('_', ' ') }}</option>
                }
              </select>
            </div>
            
            <div class="form-group">
              <label for="experienceYears">Experience Required (years) *</label>
              <input type="number" id="experienceYears" class="form-control" [(ngModel)]="formData.experienceYears" name="experienceYears" min="0" required />
            </div>
          </div>
          
          <div class="form-group">
            <label for="educationRequirement">Education Requirement *</label>
            <input type="text" id="educationRequirement" class="form-control" [(ngModel)]="formData.educationRequirement" name="educationRequirement" placeholder="e.g. Bachelor's in Computer Science" required />
          </div>
          
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
            <div class="form-group">
              <label for="applicationDeadline">Application Deadline *</label>
              <input type="date" id="applicationDeadline" class="form-control" [(ngModel)]="formData.applicationDeadline" name="applicationDeadline" required />
            </div>
            
            <div class="form-group">
              <label for="numberOfOpenings">Number of Openings *</label>
              <input type="number" id="numberOfOpenings" class="form-control" [(ngModel)]="formData.numberOfOpenings" name="numberOfOpenings" min="1" required />
            </div>
          </div>
          
          <div style="display: flex; gap: 10px;">
            <button type="submit" class="btn btn-success" [disabled]="loading">
              {{ loading ? 'Creating...' : 'Post Job' }}
            </button>
            <button type="button" (click)="cancel()" class="btn btn-secondary">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  `,
  styles: []
})
export class CreateJobComponent {
  formData: JobRequest = {
    title: '',
    description: '',
    requiredSkills: '',
    location: '',
    salaryRange: '',
    jobType: 'FULL_TIME',
    experienceYears: 0,
    educationRequirement: '',
    applicationDeadline: '',
    numberOfOpenings: 1
  };
  jobTypes: JobType[] = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'REMOTE'];
  loading = false;
  errorMessage = '';

  constructor(
    private jobService: JobService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.jobService.createJob(this.formData).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/employer/dashboard']);
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to create job';
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/employer/dashboard']);
  }
}
