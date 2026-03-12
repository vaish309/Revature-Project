import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { Application, ApplicationStatus } from '../../models/application.model';

@Component({
  selector: 'app-job-applications',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './job-applications.component.html',
  styleUrls: ['./job-applications.component.css']
})
export class JobApplicationsComponent implements OnInit {
  jobId!: number;
  applications: Application[] = [];
  selectedApplication: any = null;
  showDetailsModal = false;
  showCommentModal = false;
  commentText = '';
  selectedApplicationId: number | null = null;
  selectedStatus: ApplicationStatus | null = null;
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private applicationService: ApplicationService
  ) {}

  ngOnInit(): void {
    this.jobId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadApplications();
  }

  loadApplications(): void {
    this.loading = true;
    this.applicationService.getJobApplications(this.jobId).subscribe({
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

  viewApplicationDetails(applicationId: number): void {
    this.applicationService.getApplicationDetails(applicationId).subscribe({
      next: (response) => {
        if (response.success) {
          this.selectedApplication = response.data;
          this.showDetailsModal = true;
        }
      }
    });
  }

  closeDetailsModal(): void {
    this.showDetailsModal = false;
    this.selectedApplication = null;
  }

  downloadResume(): void {
    if (this.selectedApplication?.resume?.uploadedResumeUrl) {
      const url = `http://localhost:8080/uploads/${this.selectedApplication.resume.uploadedResumeUrl}`;
      window.open(url, '_blank');
    }
  }

  openCommentModal(applicationId: number, status: ApplicationStatus): void {
    this.selectedApplicationId = applicationId;
    this.selectedStatus = status;
    this.commentText = '';
    this.showCommentModal = true;
  }

  closeCommentModal(): void {
    this.showCommentModal = false;
    this.selectedApplicationId = null;
    this.selectedStatus = null;
    this.commentText = '';
  }

  submitStatusUpdate(): void {
    if (this.selectedApplicationId && this.selectedStatus) {
      // For SHORTLISTED and REJECTED, comment is required
      if ((this.selectedStatus === 'SHORTLISTED' || this.selectedStatus === 'REJECTED') && !this.commentText.trim()) {
        alert('Please add a comment for this action');
        return;
      }

      this.applicationService.updateApplicationStatus(
        this.selectedApplicationId,
        this.selectedStatus,
        this.commentText.trim() || undefined
      ).subscribe({
        next: () => {
          this.loadApplications();
          this.closeCommentModal();
          if (this.selectedApplication?.id === this.selectedApplicationId) {
            this.closeDetailsModal();
          }
        }
      });
    }
  }

  updateStatus(applicationId: number, status: ApplicationStatus): void {
    this.openCommentModal(applicationId, status);
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

  getStatusLabel(status: ApplicationStatus): string {
    const labels: { [key: string]: string } = {
      'UNDER_REVIEW': 'Under Review',
      'SHORTLISTED': 'Shortlist',
      'REJECTED': 'Reject'
    };
    return labels[status] || status;
  }

  goBack(): void {
    this.router.navigate(['/employer/dashboard']);
  }
}
