export interface User {
  id: number;
  email: string;
  name: string;
  role: 'JOB_SEEKER' | 'EMPLOYER';
  phone?: string;
  location?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  name: string;
  role: string;
  userId: number;
}

export interface RegisterJobSeekerRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  location: string;
  currentEmploymentStatus: string;
}

export interface RegisterEmployerRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  companyName: string;
  industry: string;
  description: string;
  website: string;
  location: string;
  companySize: number;
}
