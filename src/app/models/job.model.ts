export interface Job {
  id: number;
  title: string;
  description: string;
  requiredSkills: string;
  location: string;
  salaryRange: string;
  jobType: JobType;
  experienceYears: number;
  educationRequirement: string;
  applicationDeadline: string;
  numberOfOpenings: number;
  active: boolean;
  filled: boolean;
  postedDate: string;
  companyName: string;
  employerId: number;
  applicationCount: number;
}

export type JobType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERNSHIP' | 'REMOTE';

export interface JobRequest {
  title: string;
  description: string;
  requiredSkills: string;
  location: string;
  salaryRange: string;
  jobType: JobType;
  experienceYears: number;
  educationRequirement: string;
  applicationDeadline: string;
  numberOfOpenings: number;
}

export interface JobSearchParams {
  title?: string;
  location?: string;
  experienceYears?: number;
  jobType?: JobType;
  postedAfter?: string;
}
