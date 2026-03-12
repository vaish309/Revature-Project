import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterJobSeekerRequest } from '../../models/user.model';

@Component({
  selector: 'app-register-jobseeker',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register-jobseeker.component.html',
  styleUrls: ['./register-jobseeker.component.css']
})
export class RegisterJobSeekerComponent {
  formData: RegisterJobSeekerRequest = {
    name: '',
    email: '',
    password: '',
    phone: '',
    location: '',
    currentEmploymentStatus: 'Unemployed'
  };
  errorMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.authService.registerJobSeeker(this.formData).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/jobseeker/dashboard']);
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Registration failed. Please try again.';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}
