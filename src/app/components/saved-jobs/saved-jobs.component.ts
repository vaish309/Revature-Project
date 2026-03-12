import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { JobSeekerService } from '../../services/jobseeker.service';
import { Job } from '../../models/job.model';

@Component({
  selector: 'app-saved-jobs',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="saved-wrapper">

      <div class="page-header">
        <h1>⭐ Saved Jobs</h1>
        <p>Keep track of opportunities you're interested in.</p>
      </div>

      @if (loading) {
        <div class="loading-box">
          Loading saved jobs...
        </div>
      } 
      
      @else if (jobs.length === 0) {
        <div class="empty-card">
          <h3>No Saved Jobs Yet</h3>
          <p>Start exploring and save jobs to view them later.</p>
          <a routerLink="/jobs" class="primary-btn">Browse Jobs</a>
        </div>
      } 
      
      @else {
        <div class="jobs-grid">
          @for (job of jobs; track job.id) {
            <div class="job-card">

              <div class="job-info">
                <h3>{{ job.title }}</h3>
                <p class="company">{{ job.companyName }}</p>

                <div class="job-meta">
                  <span>📍 {{ job.location }}</span>
                  <span>💼 {{ job.jobType.replace('_', ' ') }}</span>
                  <span>💰 {{ job.salaryRange }}</span>
                </div>
              </div>

              <div class="job-actions">
                <a [routerLink]="['/jobs', job.id]" class="view-btn">
                  View
                </a>
                <button (click)="unsaveJob(job.id)" class="remove-btn">
                  Remove
                </button>
              </div>

            </div>
          }
        </div>
      }

    </div>
  `,
  styles: [`

    .saved-wrapper {
      min-height: 100vh;
      padding: 40px;
      background: linear-gradient(135deg, #1e3c72, #2a5298);
      font-family: 'Segoe UI', sans-serif;
    }

    .page-header {
      color: #fff;
      margin-bottom: 30px;
    }

    .page-header h1 {
      font-size: 28px;
      margin-bottom: 6px;
    }

    .page-header p {
      opacity: 0.85;
    }

    .loading-box {
      background: rgba(255,255,255,0.15);
      color: white;
      padding: 20px;
      border-radius: 12px;
      text-align: center;
      backdrop-filter: blur(6px);
    }

    .empty-card {
      background: white;
      padding: 40px;
      border-radius: 16px;
      text-align: center;
      box-shadow: 0 10px 25px rgba(0,0,0,0.15);
    }

    .primary-btn {
      display: inline-block;
      margin-top: 15px;
      padding: 8px 16px;
      background: #2a5298;
      color: white;
      border-radius: 6px;
      text-decoration: none;
      font-size: 14px;
      transition: 0.3s;
    }

    .primary-btn:hover {
      background: #1e3c72;
    }

    .jobs-grid {
      display: grid;
      gap: 20px;
    }

    .job-card {
      background: white;
      border-radius: 16px;
      padding: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 10px 25px rgba(0,0,0,0.12);
      transition: 0.3s ease;
    }

    .job-card:hover {
      transform: translateY(-4px);
      box-shadow: 0 15px 30px rgba(0,0,0,0.18);
    }

    .job-info h3 {
      margin: 0 0 5px;
      color: #2a5298;
    }

    .company {
      color: #666;
      font-size: 14px;
    }

    .job-meta {
      margin-top: 10px;
      display: flex;
      gap: 15px;
      font-size: 13px;
      color: #555;
      flex-wrap: wrap;
    }

    .job-actions {
      display: flex;
      gap: 10px;
    }

    .view-btn {
      padding: 6px 14px;
      background: #2a5298;
      color: white;
      border-radius: 6px;
      text-decoration: none;
      font-size: 13px;
      transition: 0.3s;
    }

    .view-btn:hover {
      background: #1e3c72;
    }

    .remove-btn {
      padding: 6px 14px;
      background: #dc3545;
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 13px;
      cursor: pointer;
      transition: 0.3s;
    }

    .remove-btn:hover {
      background: #b02a37;
    }

    @media (max-width: 768px) {
      .job-card {
        flex-direction: column;
        align-items: flex-start;
        gap: 15px;
      }

      .job-actions {
        width: 100%;
      }

      .view-btn, .remove-btn {
        flex: 1;
        text-align: center;
      }
    }

  `]
})
export class SavedJobsComponent implements OnInit {
  jobs: Job[] = [];
  loading = false;

  constructor(private jobSeekerService: JobSeekerService) {}

  ngOnInit(): void {
    this.loadSavedJobs();
  }

  loadSavedJobs(): void {
    this.loading = true;
    this.jobSeekerService.getSavedJobs().subscribe({
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

  unsaveJob(jobId: number): void {
    this.jobSeekerService.unsaveJob(jobId).subscribe({
      next: () => {
        this.loadSavedJobs();
      }
    });
  }
}