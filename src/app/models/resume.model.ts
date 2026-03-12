export interface Resume {
  id: number;
  objective?: string;
  uploadedResumeUrl?: string;
  uploadedResumeFileName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Project {
  id: number;
  projectName: string;
  description: string;
  technologies: string;
  startDate: string;
  endDate?: string;
  projectUrl?: string;
}

export interface Education {
  id: number;
  institution: string;
  degree: string;
  fieldOfStudy: string;
  startDate: string;
  endDate: string;
  gpa?: number;
  description?: string;
}

export interface Experience {
  id: number;
  company: string;
  position: string;
  location: string;
  startDate: string;
  endDate?: string;
  currentlyWorking: boolean;
  description: string;
}

export interface Skill {
  id: number;
  skillName: string;
  proficiencyLevel: ProficiencyLevel;
}

export type ProficiencyLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT';

export interface Certification {
  id: number;
  certificationName: string;
  issuingOrganization: string;
  issueDate: string;
  expiryDate?: string;
  credentialId?: string;
  credentialUrl?: string;
}
