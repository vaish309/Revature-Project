import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { JobService } from '../../services/job.service';
import { JobSeekerService } from '../../services/jobseeker.service';
import { AuthService } from '../../services/auth.service';
import { Job, JobSearchParams, JobType } from '../../models/job.model';

@Component({
  selector: 'app-job-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css']
})
export class JobListComponent implements OnInit {
  jobs: Job[] = [];
  loading = false;
  searchParams: JobSearchParams = {};
  jobTypes: JobType[] = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'REMOTE'];
  savedJobIds: Set<number> = new Set();
  saveMessages: { [jobId: number]: string } = {};

  constructor(
    private jobService: JobService,
    private jobSeekerService: JobSeekerService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadJobs();
    if (this.authService.isJobSeeker()) {
      this.loadSavedJobIds();
    }
  }

  loadJobs(): void {
    this.loading = true;
    this.jobService.searchJobs(this.searchParams).subscribe({
      next: (response) => {
        if (response.success) {
          this.jobs = response.data;
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  loadSavedJobIds(): void {
    this.jobSeekerService.getSavedJobs().subscribe({
      next: (response) => {
        if (response.success) {
          this.savedJobIds = new Set(response.data.map(j => j.id));
        }
      }
    });
  }

  isJobSaved(jobId: number): boolean {
    return this.savedJobIds.has(jobId);
  }

  toggleSaveJob(jobId: number): void {
    if (this.isJobSaved(jobId)) {
      this.jobSeekerService.unsaveJob(jobId).subscribe({
        next: () => {
          this.savedJobIds.delete(jobId);
          this.saveMessages[jobId] = 'Removed from saved!';
          setTimeout(() => delete this.saveMessages[jobId], 2000);
        }
      });
    } else {
      this.jobSeekerService.saveJob(jobId).subscribe({
        next: () => {
          this.savedJobIds.add(jobId);
          this.saveMessages[jobId] = 'Job saved!';
          setTimeout(() => delete this.saveMessages[jobId], 2000);
        },
        error: (err) => {
          this.saveMessages[jobId] = err.error?.message || 'Could not save job';
          setTimeout(() => delete this.saveMessages[jobId], 3000);
        }
      });
    }
  }

  onSearch(): void {
    this.loadJobs();
  }

  clearFilters(): void {
    this.searchParams = {};
    this.loadJobs();
  }
}
