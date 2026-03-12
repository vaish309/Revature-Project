import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { JobSeekerService } from '../../services/jobseeker.service';
import { Application } from '../../models/application.model';
import { Job } from '../../models/job.model';
import { Resume } from '../../models/resume.model';

@Component({
  selector: 'app-jobseeker-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './jobseeker-dashboard.component.html',
  styleUrls: ['./jobseeker-dashboard.component.css']
})
export class JobSeekerDashboardComponent implements OnInit {
  allApplications: Application[] = [];
  applications: Application[] = [];
  savedJobs: Job[] = [];
  allSavedJobs: Job[] = [];
  resume: Resume | null = null;
  loading = false;

  constructor(
    private applicationService: ApplicationService,
    private jobSeekerService: JobSeekerService
  ) {}

  ngOnInit(): void {
    this.loadApplications();
    this.loadSavedJobs();
    this.loadResume();
  }

  loadApplications(): void {
    this.applicationService.getMyApplications().subscribe({
      next: (response) => {
        if (response.success) {
          this.allApplications = response.data;
          this.applications = response.data.slice(0, 5);
        }
      }
    });
  }

  loadSavedJobs(): void {
    this.jobSeekerService.getSavedJobs().subscribe({
      next: (response) => {
        if (response.success) {
          this.allSavedJobs = response.data;
          this.savedJobs = response.data.slice(0, 5);
        }
      }
    });
  }

  loadResume(): void {
    this.jobSeekerService.getResume().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.resume = response.data;
        }
      },
      error: () => {
        this.resume = null;
      }
    });
  }

  getStatusClass(status: string): string {
    const statusMap: { [key: string]: string } = {
      'APPLIED': 'badge-info',
      'UNDER_REVIEW': 'badge-warning',
      'SHORTLISTED': 'badge-success',
      'REJECTED': 'badge-danger',
      'WITHDRAWN': 'badge-secondary'
    };
    return statusMap[status] || 'badge-info';
  }

  getShortlistedCount(): number {
    return this.allApplications.filter(a => a.status === 'SHORTLISTED').length;
  }

  formatStatus(status: string): string {
    return status.replace('_', ' ');
  }

  formatJobType(jobType: string): string {
    return jobType.replace('_', ' ');
  }
}
