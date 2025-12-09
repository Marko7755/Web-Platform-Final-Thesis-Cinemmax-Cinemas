import { Component } from '@angular/core';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { DecodedToken } from '../../../../backend/interfaces/decoded-token';
import { AuthService } from '../../../../backend/services/auth.service';

@Component({
  selector: 'app-cinema-manage',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cinema-manage.component.html',
  styleUrl: './cinema-manage.component.css'
})
export class CinemaManageComponent {
  public allCinemas: Cinema[] = [];
  public currentUser: Observable<DecodedToken | null> | undefined;
  constructor(private cinemaService: CinemaService, private notificationService: NotificationService, private authService: AuthService) { }

  ngOnInit(): void {
    this.getAllCinemas();
    this.currentUser = this.authService.currentUser$;
  }


  private getAllCinemas(): void {
    this.cinemaService.getAll().subscribe({
      next: (cinemas: Cinema[]) => {
        this.allCinemas = cinemas;
        console.log('Cinemas fetched successfully:', this.allCinemas);
      },
      error: (err) => {
        this.notificationService.errorNotification("Error fetching cinemas", "There was an error while fetching the cinemas!");
        console.error('Error fetching cinemas:', err);
      }
    })
  }

  deleteCinema(cinemaId: number): void {
    this.notificationService.confirmCancelNotification("delete this cinema", () => {
      this.cinemaService.delete(cinemaId).subscribe({
        next: () => {
          this.cinemaService.getAll().subscribe({
            next: (cinemas: Cinema[]) => {
              this.allCinemas = cinemas;
            }
          });
        },
        error: (err) => {
          this.notificationService.errorNotification("Error deleting film", err.error.error || "There was an error while deleting the cinema!");
          console.error('Error deleting film:', err);
        }
      })
    })
  }

}
