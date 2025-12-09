import { Component, OnInit } from '@angular/core';
import { CinemaFormBase } from '../../../../backend/classes/general/Cinema/CinemaFormBase/cinema-form-base';
import { FormBuilder } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { CinemaService } from '../../../services/cinema/cinema.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { ActivatedRoute } from '@angular/router';
import { Cinema } from '../../../../backend/classes/general/Cinema/cinema/cinema';

@Component({
  selector: 'app-cinema-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cinema-edit.component.html',
  styleUrl: './cinema-edit.component.css'
})
export class CinemaEditComponent extends CinemaFormBase implements OnInit {
  cinemaId: number | null = null;
  constructor(fb: FormBuilder, private cinenmaService: CinemaService, private notificationService: NotificationService, private route: ActivatedRoute) {
    super(fb);
  }
  ngOnInit(): void {
    const cinemaId = this.route.snapshot.paramMap.get('cinemaId');
    if (!cinemaId) {
      this.notificationService.errorNotification("Error", "Film ID is missing in the route parameters.");
      return;
    }
    this.cinemaId = +cinemaId
    this.cinenmaService.getById(+this.cinemaId!).subscribe({
      next: (cinema: Cinema) => {
        this.cinemaForm.patchValue({
          location: cinema.location,
          name: cinema.name
        });
      },
      error: (err) => {
        console.error('Error fetching film for edit:', err);
      }
    })
  }

  editCinema() {
    if (this.cinemaForm.valid) {
      const cinemaToEdit = new Cinema(
        this.cinemaForm.get('location')?.value,
        this.cinemaForm.get('name')?.value
      );
      this.cinenmaService.edit(this.cinemaId!, cinemaToEdit).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Cinema edit", res.message);
          this.cinemaForm.reset();
        },
        error: (err) => {
          this.notificationService.errorNotification("Error editing cinema", "There was an error while editing the cinema!");
          console.error(err);
        }
      })
    }
  }
} 
