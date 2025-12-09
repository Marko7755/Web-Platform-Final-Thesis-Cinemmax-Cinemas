import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from '../../../../backend/services/auth.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { User } from '../../../../backend/classes/general/User/User/user';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private notificationService: NotificationService
  ) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(4)]],
    },
      { validators: [this.passwordMatchValidator], }
    );
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const form = control as FormGroup;
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  register() {
    if (!this.registerForm.invalid) {
      const name = this.registerForm.get('name')?.value;
      const surname = this.registerForm.get('surname')?.value;
      const username = this.registerForm.get('username')?.value;
      const email = this.registerForm.get('email')?.value;
      const password = this.registerForm.get('password')?.value;
      const confirmPassword = this.registerForm.get('confirmPassword')?.value;
      const newUser = new User(name, surname, username, email, password, confirmPassword);
      console.log(newUser);

      this.authService.register(newUser).subscribe({
        next: () => {
          this.notificationService.successNotificationRegisterWithRedirection("Registration successful", "You've been successfully registered!");
        },
        error: err => {
          if (err.status === 400) {
            this.notificationService.errorNotification("Error registering", err.error.message);
            return;
          }
          if (err.status === 409) {
            this.notificationService.errorNotification("Error registering", err.error.error);
            return;
          }
          this.notificationService.errorNotification("Error registering", `There was an error while registering, please try again`);
          console.error(err);
        }
      })

    }
    else {
      this.registerForm.markAllAsTouched();
    }
  }


}
