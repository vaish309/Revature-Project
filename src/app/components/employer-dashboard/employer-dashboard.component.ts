import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { JobService } from '../../services/job.service';
import { Job } from '../../models/job.model';

@Component({
  selector: 'app-employer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <div class="dashboard-container">

    <!-- Header -->
    <div class="dashboard-header">
      <h1>Welcome, Employer</h1>
      <a routerLink="/employer/jobs/create" class="btn btn-gradient">Post New Job</a>
    </div>

    <!-- Stats Section -->
    <div class="stats-grid">
      <div class="stat-card">
        <h2>{{ jobs.length }}</h2>
        <p>Total Jobs Posted</p>
      </div>
      <div class="stat-card">
        <h2>{{ activeJobs }}</h2>
        <p>Active Jobs</p>
      </div>
      <div class="stat-card">
        <h2>{{ totalApplications }}</h2>
        <p>Total Applications</p>
      </div>
    </div>

    <!-- Job Listings -->
    <h2 class="section-title">My Job Postings</h2>

    <div *ngIf="loading" class="loading">Loading jobs...</div>
    <div *ngIf="!loading && jobs.length === 0" class="empty-state">
      <p>You haven't posted any jobs yet</p>
      <a routerLink="/employer/jobs/create" class="btn btn-gradient">Post Your First Job</a>
    </div>

    <div class="job-list" *ngIf="!loading && jobs.length > 0">
      <div *ngFor="let job of jobs" class="job-card">
        <div class="job-info">
          <h3>{{ job.title }}</h3>
          <p class="job-meta">
            📍 {{ job.location }} • 💼 {{ job.jobType.replace('_',' ') }} • 📅 {{ job.postedDate | date }}
          </p>
          <p class="applications">Applications: {{ job.applicationCount }}</p>
          <span class="status-badge" [ngClass]="job.active ? 'active' : 'inactive'">
            {{ job.active ? 'Active' : 'Inactive' }}
          </span>
        </div>
        <div class="job-actions">
          <a [routerLink]="['/employer/jobs', job.id, 'applications']" class="btn btn-primary">View Applications</a>
          <a [routerLink]="['/employer/jobs', job.id, 'edit']" class="btn btn-secondary">Edit</a>
          <button (click)="toggleJobStatus(job.id)" class="btn btn-secondary">
            {{ job.active ? 'Deactivate' : 'Activate' }}
          </button>
          <button (click)="deleteJob(job.id)" class="btn btn-danger">Delete</button>
        </div>
      </div>
    </div>

  </div>
  `,
  styles: [`
    /* Dashboard Container */
    .dashboard-container {
      padding: 40px 20px;
      background-color: #f4f5f9;
      min-height: 100vh;
      font-family: 'Segoe UI', sans-serif;
    }

    /* Header */
    .dashboard-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 50px;
      flex-wrap: wrap;
      gap: 20px;
    }
    .dashboard-header h1 {
      font-size: 36px;
      color: #764ba2;
    }
    .btn-gradient {
      background: linear-gradient(135deg, #667eea, #764ba2);
      color: white;
      padding: 12px 30px;
      border-radius: 12px;
      font-weight: 600;
      text-decoration: none;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .btn-gradient:hover {
      transform: translateY(-3px);
      box-shadow: 0 15px 25px rgba(0,0,0,0.2);
    }

    /* Stats Grid */
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 25px;
      margin-bottom: 60px;
    }
    .stat-card {
      background: white;
      border-radius: 20px;
      padding: 40px 20px;
      text-align: center;
      box-shadow: 0 15px 30px rgba(0,0,0,0.05);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }
    .stat-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 20px 40px rgba(0,0,0,0.1);
    }
    .stat-card h2 {
      font-size: 40px;
      color: #667eea;
      margin-bottom: 10px;
    }
    .stat-card p {
      color: #555;
      font-weight: 500;
    }

    /* Section Title */
    .section-title {
      font-size: 28px;
      color: #764ba2;
      margin-bottom: 25px;
      font-weight: 700;
    }

    /* Empty State */
    .empty-state {
      background: #ffffff;
      border-radius: 20px;
      padding: 40px;
      text-align: center;
      box-shadow: 0 15px 30px rgba(0,0,0,0.05);
    }
    .empty-state p {
      margin-bottom: 20px;
      font-size: 18px;
      color: #555;
    }

    /* Job List */
    .job-list {
      display: flex;
      flex-direction: column;
      gap: 25px;
    }
    .job-card {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      background: white;
      border-radius: 20px;
      padding: 25px;
      box-shadow: 0 15px 30px rgba(0,0,0,0.05);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      flex-wrap: wrap;
      gap: 20px;
    }
    .job-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 20px 40px rgba(0,0,0,0.1);
    }
    .job-info h3 {
      color: #667eea;
      margin-bottom: 8px;
    }
    .job-meta {
      font-size: 14px;
      color: #666;
      margin-bottom: 10px;
    }
    .applications {
      font-weight: 600;
      color: #764ba2;
    }
    .status-badge {
      padding: 6px 15px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 14px;
      display: inline-block;
      margin-top: 10px;
    }
    .status-badge.active {
      background-color: #28a745;
      color: white;
    }
    .status-badge.inactive {
      background-color: #dc3545;
      color: white;
    }

    /* Job Actions */
    .job-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
    .job-actions .btn {
      padding: 10px 18px;
      border-radius: 12px;
      font-size: 14px;
      font-weight: 600;
    }
    .btn-primary { background-color: #667eea; color: white; }
    .btn-primary:hover { opacity: 0.9; }
    .btn-secondary { background-color: #764ba2; color: white; }
    .btn-secondary:hover { opacity: 0.9; }
    .btn-danger { background-color: #dc3545; color: white; }
    .btn-danger:hover { opacity: 0.9; }

    /* Loading */
    .loading {
      text-align: center;
      font-size: 18px;
      padding: 50px 0;
      color: #555;
    }

    /* Responsive */
    @media(max-width: 768px) {
      .job-card { flex-direction: column; }
      .job-actions { justify-content: flex-start; }
      .dashboard-header { flex-direction: column; align-items: flex-start; }
    }
  `]
})
export class EmployerDashboardComponent implements OnInit {
  jobs: Job[] = [];
  loading = false;
  activeJobs = 0;
  totalApplications = 0;

  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.loading = true;
    this.jobService.getMyJobs().subscribe({
      next: (response) => {
        if (response.success) {
          this.jobs = response.data;
          this.activeJobs = this.jobs.filter(j => j.active).length;
          this.totalApplications = this.jobs.reduce((sum, j) => sum + j.applicationCount, 0);
        }
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  toggleJobStatus(id: number): void {
    this.jobService.toggleJobStatus(id).subscribe({ next: () => this.loadJobs() });
  }

  deleteJob(id: number): void {
    if (confirm('Are you sure you want to delete this job?')) {
      this.jobService.deleteJob(id).subscribe({ next: () => this.loadJobs() });
    }
  }
}