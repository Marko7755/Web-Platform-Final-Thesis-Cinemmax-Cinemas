import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../backend/services/auth.service';
import { UserInfo } from '../../../backend/classes/general/User/UserInfo/user-info'; 

@Component({
  selector: 'app-my-profile',
  standalone: true,
  imports: [],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.css'
})
export class MyProfileComponent implements OnInit {
  public user: UserInfo | null = null;
  constructor(private authService: AuthService) { }
  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user: UserInfo) => {
        this.user = user;
      },
      error: (err) => {
        console.error('Error fetching current user:', err);
      }
    });
  }
}
