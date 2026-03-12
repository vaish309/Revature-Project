import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterEmployerRequest } from '../../models/user.model';

@Component({
  selector: 'app-register-employer',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register-employer.component.html',
  styleUrls: ['./register-employer.component.css']
})
export class RegisterEmployerComponent {
  formData: RegisterEmployerRequest = {
    name: '',
    email: '',
    password: '',
    phone: '',
    companyName: '',
    industry: '',
    description: '',
    website: '',
    location: '',
    companySize: 0
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

    this.authService.registerEmployer(this.formData).subscribe({
      next: (response) => {
        if (response.success) {
          this.router.navigate(['/employer/dashboard']);
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
