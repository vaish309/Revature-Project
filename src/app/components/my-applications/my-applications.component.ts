import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { Application } from '../../models/application.model';

@Component({
  selector: 'app-my-applications',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="applications-wrapper">
      
      <!-- Header -->
      <div class="page-header">
        <h1>My Applications</h1>
        <p>Track and manage your job applications effortlessly</p>
      </div>

      <!-- Loading Spinner -->
      <div *ngIf="loading" class="loading-box">
        <div class="spinner"></div>
        <p>Loading your applications...</p>
      </div>

      <!-- Empty State -->
      <div *ngIf="!loading && applications.length === 0" class="empty-state">
        <div class="empty-icon">📄</div>
        <h3>No Applications Yet</h3>
        <p>Start applying to jobs and track your progress here.</p>
        <a routerLink="/jobs" class="browse-btn">Browse Jobs</a>
      </div>

      <!-- Applications Grid -->
      <div *ngIf="!loading && applications.length > 0" class="applications-grid">
        <div *ngFor="let app of applications; trackBy: trackByAppId" class="application-card">

          <div class="card-top">
            <div>
              <h3 class="job-title">{{ app.jobTitle }}</h3>
              <p class="company-name">{{ app.companyName }}</p>
            </div>

            <span class="status-badge" [ngClass]="getStatusClass(app.status)">
              {{ app.status.replace('_', ' ') }}
            </span>
          </div>

          <div class="card-meta">
            Applied on {{ app.appliedAt | date:'mediumDate' }}
          </div>

          <div *ngIf="app.employerComments" class="feedback-box">
            <strong>Employer Feedback</strong>
            <p>{{ app.employerComments }}</p>
          </div>

          <div *ngIf="app.status === 'APPLIED' || app.status === 'UNDER_REVIEW'" class="card-actions">
            <button (click)="withdrawApplication(app.id)" class="withdraw-btn">
              Withdraw Application
            </button>
          </div>

        </div>
      </div>

    </div>
  `,
  styles: [`
    /* Wrapper */
    .applications-wrapper {
      max-width: 1200px;
      margin: 50px auto;
      padding: 0 20px;
      font-family: 'Inter', sans-serif;
    }

    body {
      background: linear-gradient(135deg, #f0f4ff, #fef9ff);
    }

    /* Header */
    .page-header h1 {
      font-size: 36px;
      font-weight: 800;
      background: linear-gradient(90deg, #4f46e5, #f43f5e);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin-bottom: 6px;
    }

    .page-header p {
      font-size: 16px;
      color: #6b7280;
      margin-bottom: 30px;
    }

    /* Loading Spinner */
    .loading-box {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;
      padding: 40px 0;
    }

    .spinner {
      width: 50px;
      height: 50px;
      border: 5px solid #e5e7eb;
      border-top-color: #4f46e5;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    /* Empty State */
    .empty-state {
      text-align: center;
      padding: 60px 20px;
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 12px 30px rgba(0,0,0,0.05);
      transition: transform 0.3s ease;
    }

    .empty-state:hover {
      transform: translateY(-3px);
    }

    .empty-icon {
      font-size: 50px;
      margin-bottom: 20px;
    }

    .browse-btn {
      display: inline-block;
      margin-top: 20px;
      padding: 10px 25px;
      border-radius: 12px;
      background: linear-gradient(90deg, #4f46e5, #f43f5e);
      color: white;
      text-decoration: none;
      font-weight: 600;
      transition: 0.3s ease;
    }

    .browse-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 15px rgba(244,63,94,0.25);
    }

    /* Applications Grid */
    .applications-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 25px;
    }

    .application-card {
      background: #ffffff;
      border-radius: 16px;
      padding: 22px;
      border: 1px solid #e5e7eb;
      position: relative;
      overflow: hidden;
      transition: all 0.3s ease;
    }

    .application-card:hover {
      box-shadow: 0 20px 40px rgba(79,70,229,0.15);
      transform: translateY(-5px);
    }

    .application-card::before {
      content: "";
      position: absolute;
      left: 0;
      top: 0;
      width: 6px;
      height: 100%;
      background: linear-gradient(to bottom, #4f46e5, #f43f5e);
      border-radius: 16px 0 0 16px;
    }

    .card-top {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      gap: 10px;
    }

    .job-title {
      font-size: 20px;
      font-weight: 700;
      color: #1e293b;
      margin: 0;
    }

    .company-name {
      font-size: 14px;
      color: #6b7280;
      margin-top: 4px;
    }

    .card-meta {
      margin-top: 10px;
      font-size: 13px;
      color: #94a3b8;
    }

    /* Status Badge */
    .status-badge {
      padding: 6px 16px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: 0.3px;
      text-transform: uppercase;
    }

    .badge-info {
      background: #e0e7ff;
      color: #4338ca;
    }

    .badge-warning {
      background: #fff7ed;
      color: #b45309;
    }

    .badge-success {
      background: #dcfce7;
      color: #15803d;
    }

    .badge-danger {
      background: #fee2e2;
      color: #b91c1c;
    }

    .badge-secondary {
      background: #f3f4f6;
      color: #4b5563;
    }

    /* Feedback Box */
    .feedback-box {
      margin-top: 15px;
      background: linear-gradient(135deg, #f9fafb, #e5e7eb);
      padding: 15px;
      border-radius: 12px;
      font-size: 14px;
      color: #1e293b;
    }

    .feedback-box strong {
      display: block;
      margin-bottom: 6px;
    }

    /* Withdraw Button */
    .card-actions {
      margin-top: 18px;
      text-align: right;
    }

    .withdraw-btn {
      padding: 10px 20px;
      border-radius: 12px;
      border: none;
      background: linear-gradient(90deg, #ef4444, #b91c1c);
      color: white;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      transition: 0.3s ease;
    }

    .withdraw-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 15px rgba(220,38,38,0.25);
    }
  `]
})
export class MyApplicationsComponent implements OnInit {

  applications: Application[] = [];
  loading = false;

  constructor(private applicationService: ApplicationService) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  trackByAppId(index: number, app: Application): number {
    return app.id;
  }

  loadApplications(): void {
    this.loading = true;
    this.applicationService.getMyApplications().subscribe({
      next: (response) => {
        if (response.success) {
          this.applications = response.data;
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  withdrawApplication(id: number): void {
    if (confirm('Are you sure you want to withdraw this application?')) {
      this.applicationService.withdrawApplication(id).subscribe({
        next: () => this.loadApplications()
      });
    }
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
}