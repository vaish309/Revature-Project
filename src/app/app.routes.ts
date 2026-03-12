import { Routes } from '@angular/router';
import { authGuard, jobSeekerGuard, employerGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./components/home/home.component').then(m => m.HomeComponent) },
  { path: 'login', loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent) },
  { path: 'register/jobseeker', loadComponent: () => import('./components/register-jobseeker/register-jobseeker.component').then(m => m.RegisterJobSeekerComponent) },
  { path: 'register/employer', loadComponent: () => import('./components/register-employer/register-employer.component').then(m => m.RegisterEmployerComponent) },
  { path: 'jobs', loadComponent: () => import('./components/job-list/job-list.component').then(m => m.JobListComponent) },
  { path: 'jobs/:id', loadComponent: () => import('./components/job-detail/job-detail.component').then(m => m.JobDetailComponent) },
  { 
    path: 'jobseeker/dashboard', 
    loadComponent: () => import('./components/jobseeker-dashboard/jobseeker-dashboard.component').then(m => m.JobSeekerDashboardComponent),
    canActivate: [jobSeekerGuard]
  },
  { 
    path: 'jobseeker/applications', 
    loadComponent: () => import('./components/my-applications/my-applications.component').then(m => m.MyApplicationsComponent),
    canActivate: [jobSeekerGuard]
  },
  { 
    path: 'jobseeker/resume', 
    loadComponent: () => import('./components/resume-builder/resume-builder.component').then(m => m.ResumeBuilderComponent),
    canActivate: [jobSeekerGuard]
  },
  { 
    path: 'jobseeker/saved-jobs', 
    loadComponent: () => import('./components/saved-jobs/saved-jobs.component').then(m => m.SavedJobsComponent),
    canActivate: [jobSeekerGuard]
  },
  { 
    path: 'employer/dashboard', 
    loadComponent: () => import('./components/employer-dashboard/employer-dashboard.component').then(m => m.EmployerDashboardComponent),
    canActivate: [employerGuard]
  },
  { 
    path: 'employer/jobs/create', 
    loadComponent: () => import('./components/create-job/create-job.component').then(m => m.CreateJobComponent),
    canActivate: [employerGuard]
  },
  { 
    path: 'employer/jobs/:id/edit', 
    loadComponent: () => import('./components/edit-job/edit-job.component').then(m => m.EditJobComponent),
    canActivate: [employerGuard]
  },
  { 
    path: 'employer/jobs/:id/applications', 
    loadComponent: () => import('./components/job-applications/job-applications.component').then(m => m.JobApplicationsComponent),
    canActivate: [employerGuard]
  },
  { path: '**', redirectTo: '' }
];
