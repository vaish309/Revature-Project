import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { JobService } from '../../services/job.service';
import { ApplicationService } from '../../services/application.service';
import { JobSeekerService } from '../../services/jobseeker.service';
import { AuthService } from '../../services/auth.service';
import { Job } from '../../models/job.model';

@Component({
  selector: 'app-job-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './job-detail.component.html',
  styleUrls: ['./job-detail.component.css']
})
export class JobDetailComponent implements OnInit {
  job: Job | null = null;
  loading = false;
  showApplyForm = false;
  applying = false;
  successMessage = '';
  errorMessage = '';
  isJobSaved = false;
  saveMessage = '';

  selectedResumeFile: File | null = null;
  resumeUploadError = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobService: JobService,
    private applicationService: ApplicationService,
    private jobSeekerService: JobSeekerService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadJob(id);
    if (this.authService.isJobSeeker()) {
      this.checkIfSaved(id);
    }
  }

  checkIfSaved(jobId: number): void {
    this.jobSeekerService.getSavedJobs().subscribe({
      next: (response) => {
        if (response.success) {
          this.isJobSaved = response.data.some(j => j.id === jobId);
        }
      }
    });
  }

  toggleSaveJob(): void {
    if (!this.job) return;
    const jobId = this.job.id;
    if (this.isJobSaved) {
      this.jobSeekerService.unsaveJob(jobId).subscribe({
        next: () => {
          this.isJobSaved = false;
          this.saveMessage = 'Job removed from saved!';
          setTimeout(() => this.saveMessage = '', 2000);
        }
      });
    } else {
      this.jobSeekerService.saveJob(jobId).subscribe({
        next: () => {
          this.isJobSaved = true;
          this.saveMessage = 'Job saved successfully!';
          setTimeout(() => this.saveMessage = '', 2000);
        },
        error: (err) => {
          this.saveMessage = err.error?.message || 'Could not save job';
          setTimeout(() => this.saveMessage = '', 3000);
        }
      });
    }
  }

  loadJob(id: number): void {
    this.loading = true;
    this.jobService.getJobById(id).subscribe({
      next: (response) => {
        if (response.success) {
          this.job = response.data;
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/jobs']);
      }
    });
  }

  toggleApplyForm(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.showApplyForm = !this.showApplyForm;
    this.errorMessage = '';
    this.successMessage = '';
    this.resumeUploadError = '';
    this.selectedResumeFile = null;
  }

  onResumeFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    const allowedTypes = [
      'application/pdf',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    ];
    if (!allowedTypes.includes(file.type)) {
      this.resumeUploadError = 'Only PDF and DOCX files are allowed';
      this.selectedResumeFile = null;
      return;
    }
    if (file.size > 2 * 1024 * 1024) {
      this.resumeUploadError = 'File size must be less than 2MB';
      this.selectedResumeFile = null;
      return;
    }

    this.selectedResumeFile = file;
    this.resumeUploadError = '';
  }

  onApply(): void {
    if (!this.job) return;

    if (!this.selectedResumeFile) {
      this.resumeUploadError = 'Please upload your resume to apply';
      return;
    }

    this.applying = true;
    this.errorMessage = '';
    this.successMessage = '';

    // Step 1: Upload resume to backend (saves it on the jobseeker profile)
    this.jobSeekerService.uploadResume(this.selectedResumeFile).subscribe({
      next: () => {
        // Step 2: Submit application — backend only needs jobId
        this.applicationService.applyForJob({ jobId: this.job!.id, resumeId: 0 }).subscribe({
          next: (response) => {
            if (response.success) {
              this.successMessage = 'Application submitted! Track it in My Applications.';
              this.showApplyForm = false;
              this.selectedResumeFile = null;
              this.loadJob(this.job!.id);
            } else {
              this.errorMessage = response.message || 'Failed to submit application';
            }
            this.applying = false;
          },
          error: (error) => {
            this.errorMessage = error.error?.message || 'Failed to submit application';
            this.applying = false;
          }
        });
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to upload resume';
        this.applying = false;
      }
    });
  }
}
