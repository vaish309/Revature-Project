import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { JobService } from '../../services/job.service';
import { JobRequest, JobType } from '../../models/job.model';

@Component({
  selector: 'app-edit-job',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container" style="padding: 40px 0;">
      <h1>Edit Job</h1>
      
      @if (loading) {
        <div class="loading">Loading job details...</div>
      } @else {
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
              <input type="text" id="requiredSkills" class="form-control" [(ngModel)]="formData.requiredSkills" name="requiredSkills" required />
            </div>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
              <div class="form-group">
                <label for="location">Location *</label>
                <input type="text" id="location" class="form-control" [(ngModel)]="formData.location" name="location" required />
              </div>
              
              <div class="form-group">
                <label for="salaryRange">Salary Range *</label>
                <input type="text" id="salaryRange" class="form-control" [(ngModel)]="formData.salaryRange" name="salaryRange" required />
              </div>
            </div>
            
            <div style="display: flex; gap: 10px;">
              <button type="submit" class="btn btn-success" [disabled]="saving">
                {{ saving ? 'Saving...' : 'Save Changes' }}
              </button>
              <button type="button" (click)="cancel()" class="btn btn-secondary">Cancel</button>
            </div>
          </form>
        </div>
      }
    </div>
  `,
  styles: []
})
export class EditJobComponent implements OnInit {
  jobId!: number;
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
  loading = false;
  saving = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobService: JobService
  ) {}

  ngOnInit(): void {
    this.jobId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadJob();
  }

  loadJob(): void {
    this.loading = true;
    this.jobService.getJobById(this.jobId).subscribe({
      next: (response) => {
        if (response.success) {
          const job = response.data;
          this.formData = {
            title: job.title,
            description: job.description,
            requiredSkills: job.requiredSkills,
            location: job.location,
            salaryRange: job.salaryRange,
            jobType: job.jobType,
            experienceYears: job.experienceYears,
            educationRequirement: job.educationRequirement,
            applicationDeadline: job.applicationDeadline,
            numberOfOpenings: job.numberOfOpenings
          };
        }
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    this.saving = true;
    this.jobService.updateJob(this.jobId, this.formData).subscribe({
      next: () => {
        this.router.navigate(['/employer/dashboard']);
      },
      error: () => {
        this.saving = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/employer/dashboard']);
  }
}
