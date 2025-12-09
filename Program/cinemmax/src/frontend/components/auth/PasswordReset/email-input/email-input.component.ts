import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NotificationService } from '../../../../services/notification/notification.service';
import { AuthService } from '../../../../../backend/services/auth.service';

@Component({
  selector: 'app-email-input',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './email-input.component.html',
  styleUrl: './email-input.component.css'
})
export class EmailInputComponent {
  emailResetForm: FormGroup;
  emailSent: boolean = false;
  isLoading: boolean = false;

  constructor(private fb: FormBuilder, private router: Router,
    private notificationService: NotificationService, private authService: AuthService) {
    this.emailResetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submitEmailReset() {
    if (!this.emailResetForm.invalid) {
      const email = this.emailResetForm.value.email;
      this.isLoading = true;

      console.log('Reset link will be sent to:', email);

      this.authService.sendEmail(email).subscribe({
        next: () => {
          console.log('Email sent successfully');
          this.emailSent = true;
          this.isLoading = false;
        },
        error: err => {
          let errorMessage = "There was an error sending the reset link. Please try again.";

          try {
            const parsed = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
            errorMessage = parsed?.errorRRR || errorMessage;
          } catch {}

          this.notificationService.errorNotification("Email Sending Failed", errorMessage);
          console.error(err);
          this.isLoading = false;
        }

      })

    }
    else {
      this.emailResetForm.markAllAsTouched();
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

}
