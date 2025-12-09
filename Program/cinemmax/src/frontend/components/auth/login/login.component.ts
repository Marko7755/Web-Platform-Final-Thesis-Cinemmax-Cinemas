import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../../backend/services/auth.service';
import { Credentials } from '../../../../backend/classes/general/Credentials/credentials';
import { ReactiveFormsModule } from '@angular/forms';
import { NotificationService } from '../../../services/notification/notification.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(20)    
      ])],
      password: ['', Validators.compose([
        Validators.required,
        Validators.minLength(4)
      ])],
    });
  }


  login() {
    if(!this.loginForm.invalid) {
      const username = this.loginForm.controls['username'].value;
      const password = this.loginForm.controls['password'].value; 
      let credentials: Credentials = new Credentials(username, password);
      /* console.log(credentials); */
      this.authService.login(credentials).subscribe( {
        next: () => {
          this.notificationService.authentificationMessageSuccess("Logged in!", "You've successfully logged in!");
          this.authService.updateUserRoleFromToken();
          const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/';
          this.router.navigateByUrl(returnUrl);
        },
        error: err => {
          if(err.status !== 0) {
            this.notificationService.errorNotification("Error logging in", "Wrong username or password");
            console.error(err);
          }
          
        }
      });
    }
    else {
      this.loginForm.markAllAsTouched();
      return;
    }
  }



}
