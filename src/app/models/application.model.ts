export interface Application {
  id: number;
  jobId: number;
  jobTitle: string;
  companyName: string;
  status: ApplicationStatus;
  appliedAt: string;
  coverLetter?: string;
  applicantName?: string;
  applicantEmail?: string;
  employerComments?: string;
  resumeId?: number;
  resumeTitle?: string;
}

export type ApplicationStatus = 'APPLIED' | 'UNDER_REVIEW' | 'SHORTLISTED' | 'REJECTED' | 'WITHDRAWN';

export interface ApplicationRequest {
  jobId: number;
  coverLetter?: string;
  resumeId: number;
}
