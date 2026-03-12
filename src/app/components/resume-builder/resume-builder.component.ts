import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { JobSeekerService } from '../../services/jobseeker.service';
import { Resume, Education, Experience, Skill, Certification } from '../../models/resume.model';

@Component({
  selector: 'app-resume-builder',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './resume-builder.component.html',
  styleUrls: ['./resume-builder.component.css']
})
export class ResumeBuilderComponent implements OnInit {
  activeTab: 'objective' | 'education' | 'experience' | 'skills' | 'certifications' | 'upload' = 'objective';
  
  resume: Resume | null = null;
  objective: string = '';
  
  educationList: Education[] = [];
  experienceList: Experience[] = [];
  skillsList: Skill[] = [];
  certificationsList: Certification[] = [];
  
  educationForm: Partial<Education> = {};
  experienceForm: Partial<Experience> = { currentlyWorking: false };
  skillForm: Partial<Skill> = { proficiencyLevel: 'INTERMEDIATE' };
  certificationForm: Partial<Certification> = {};
  
  selectedFile: File | null = null;
  uploadError: string = '';
  
  loading: boolean = false;
  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private jobSeekerService: JobSeekerService) {}

  ngOnInit(): void {
    this.loadResumeData();
  }

  loadResumeData(): void {
    this.loading = true;
    
    this.jobSeekerService.getResume().subscribe({
      next: (response) => {
        this.resume = response.data;
        this.objective = this.resume?.objective || '';
      },
      error: () => {
        this.resume = null;
      }
    });
    
    this.jobSeekerService.getEducation().subscribe({
      next: (response) => this.educationList = response.data,
      error: () => this.educationList = []
    });
    
    this.jobSeekerService.getExperience().subscribe({
      next: (response) => this.experienceList = response.data,
      error: () => this.experienceList = []
    });
    
    this.jobSeekerService.getSkills().subscribe({
      next: (response) => this.skillsList = response.data,
      error: () => this.skillsList = []
    });
    
    this.jobSeekerService.getCertifications().subscribe({
      next: (response) => {
        this.certificationsList = response.data;
        this.loading = false;
      },
      error: () => {
        this.certificationsList = [];
        this.loading = false;
      }
    });
  }

  saveObjective(): void {
    if (!this.objective.trim()) {
      this.showMessage('Please enter an objective', 'error');
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.createOrUpdateResume(this.objective).subscribe({
      next: (response) => {
        this.resume = response.data;
        this.showMessage('Objective saved successfully!', 'success');
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Failed to save objective: ' + (error.error?.message || 'Unknown error'), 'error');
        this.loading = false;
      }
    });
  }

  addEducation(): void {
    if (!this.educationForm.institution || !this.educationForm.degree) {
      this.showMessage('Please fill in required fields', 'error');
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.addEducation(this.educationForm).subscribe({
      next: (response) => {
        this.educationList.push(response.data);
        this.educationForm = {};
        this.showMessage('Education added successfully!', 'success');
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Failed to add education: ' + (error.error?.message || 'Unknown error'), 'error');
        this.loading = false;
      }
    });
  }

  deleteEducation(id: number): void {
    if (!confirm('Are you sure you want to delete this education entry?')) return;
    
    this.jobSeekerService.deleteEducation(id).subscribe({
      next: () => {
        this.educationList = this.educationList.filter(e => e.id !== id);
        this.showMessage('Education deleted successfully!', 'success');
      },
      error: (error) => {
        this.showMessage('Failed to delete education: ' + (error.error?.message || 'Unknown error'), 'error');
      }
    });
  }

  addExperience(): void {
    if (!this.experienceForm.company || !this.experienceForm.position) {
      this.showMessage('Please fill in required fields', 'error');
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.addExperience(this.experienceForm).subscribe({
      next: (response) => {
        this.experienceList.push(response.data);
        this.experienceForm = { currentlyWorking: false };
        this.showMessage('Experience added successfully!', 'success');
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Failed to add experience: ' + (error.error?.message || 'Unknown error'), 'error');
        this.loading = false;
      }
    });
  }

  deleteExperience(id: number): void {
    if (!confirm('Are you sure you want to delete this experience entry?')) return;
    
    this.jobSeekerService.deleteExperience(id).subscribe({
      next: () => {
        this.experienceList = this.experienceList.filter(e => e.id !== id);
        this.showMessage('Experience deleted successfully!', 'success');
      },
      error: (error) => {
        this.showMessage('Failed to delete experience: ' + (error.error?.message || 'Unknown error'), 'error');
      }
    });
  }

  addSkill(): void {
    if (!this.skillForm.skillName) {
      this.showMessage('Please enter a skill name', 'error');
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.addSkill(this.skillForm).subscribe({
      next: (response) => {
        this.skillsList.push(response.data);
        this.skillForm = { proficiencyLevel: 'INTERMEDIATE' };
        this.showMessage('Skill added successfully!', 'success');
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Failed to add skill: ' + (error.error?.message || 'Unknown error'), 'error');
        this.loading = false;
      }
    });
  }

  deleteSkill(id: number): void {
    if (!confirm('Are you sure you want to delete this skill?')) return;
    
    this.jobSeekerService.deleteSkill(id).subscribe({
      next: () => {
        this.skillsList = this.skillsList.filter(s => s.id !== id);
        this.showMessage('Skill deleted successfully!', 'success');
      },
      error: (error) => {
        this.showMessage('Failed to delete skill: ' + (error.error?.message || 'Unknown error'), 'error');
      }
    });
  }

  addCertification(): void {
    if (!this.certificationForm.certificationName || !this.certificationForm.issuingOrganization) {
      this.showMessage('Please fill in required fields', 'error');
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.addCertification(this.certificationForm).subscribe({
      next: (response) => {
        this.certificationsList.push(response.data);
        this.certificationForm = {};
        this.showMessage('Certification added successfully!', 'success');
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Failed to add certification: ' + (error.error?.message || 'Unknown error'), 'error');
        this.loading = false;
      }
    });
  }

  deleteCertification(id: number): void {
    if (!confirm('Are you sure you want to delete this certification?')) return;
    
    this.jobSeekerService.deleteCertification(id).subscribe({
      next: () => {
        this.certificationsList = this.certificationsList.filter(c => c.id !== id);
        this.showMessage('Certification deleted successfully!', 'success');
      },
      error: (error) => {
        this.showMessage('Failed to delete certification: ' + (error.error?.message || 'Unknown error'), 'error');
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;
    
    const allowedTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (!allowedTypes.includes(file.type)) {
      this.uploadError = 'Only PDF and DOCX files are allowed';
      this.selectedFile = null;
      return;
    }
    
    if (file.size > 2 * 1024 * 1024) {
      this.uploadError = 'File size must be less than 2MB';
      this.selectedFile = null;
      return;
    }
    
    this.selectedFile = file;
    this.uploadError = '';
  }

  uploadResumeFile(): void {
    if (!this.selectedFile) {
      this.uploadError = 'Please select a file';
      return;
    }
    
    this.loading = true;
    this.jobSeekerService.uploadResume(this.selectedFile).subscribe({
      next: (response) => {
        this.resume = response.data;
        this.selectedFile = null;
        this.uploadError = '';
        this.showMessage('Resume uploaded successfully!', 'success');
        this.loading = false;
        
        const fileInput = document.getElementById('resumeFile') as HTMLInputElement;
        if (fileInput) fileInput.value = '';
      },
      error: (error) => {
        this.uploadError = 'Failed to upload resume: ' + (error.error?.message || 'Unknown error');
        this.loading = false;
      }
    });
  }

  showMessage(msg: string, type: 'success' | 'error'): void {
    this.message = msg;
    this.messageType = type;
    setTimeout(() => {
      this.message = '';
    }, 5000);
  }
}
