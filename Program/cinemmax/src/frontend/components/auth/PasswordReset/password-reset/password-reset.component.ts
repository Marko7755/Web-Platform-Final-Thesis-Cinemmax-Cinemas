import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors, AbstractControl } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { NotificationService } from '../../../../services/notification/notification.service';
import { AuthService } from '../../../../../backend/services/auth.service';

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './password-reset.component.html',
  styleUrl: './password-reset.component.css'
})
export class PasswordResetComponent {
  resetForm: FormGroup;
  token: string = '';

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private notificationService: NotificationService, private authService: AuthService) {
    this.resetForm = this.fb.group(
      {
        password: ['', [Validators.required, Validators.minLength(4)]],
        confirmPassword: ['', [Validators.required]],
      },
      { validators: this.passwordMatchValidator }
    );

    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
    if (!this.token) {
      console.error('No token provided in the URL');
    }


  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  resetPassword() {
    if (!this.resetForm.invalid) {
      const newPassword = this.resetForm.get('password')?.value;
      const passwordConfirmation = this.resetForm.get('confirmPassword')?.value;
      console.log(newPassword, "-->", passwordConfirmation);
      console.log(this.token);

      this.authService.passwordReset(this.token, newPassword, passwordConfirmation).subscribe({
        next: (response) => {
           this.notificationService.authentificationMessageSuccess(
            "Password Reset Successful",
            response.message  
          );
          console.log(this.token);
        },
         
        error: err => {
          this.notificationService.errorNotification("Password Reset Failed", err.error?.error || "There was an error resetting your password");
          console.error(err);
        },
      })
    }
    else {
      this.resetForm.markAllAsTouched();
    }
  }
}
