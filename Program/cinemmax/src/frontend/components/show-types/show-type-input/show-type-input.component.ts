import { Component } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { ShowType } from '../../../../backend/classes/general/ShowType/show-type';
import { ShowTypeService } from '../../../services/show-type/show-type.service';
import { NotificationService } from '../../../services/notification/notification.service';

@Component({
  selector: 'app-show-type-input',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './show-type-input.component.html',
  styleUrl: './show-type-input.component.css'
})
export class ShowTypeInputComponent {
  showType: ShowType = new ShowType('', 0.0);
  constructor(private showTypeService: ShowTypeService, private notificationService: NotificationService) { }

  submitShowType(form: NgForm) {
    if(form.valid) {
      const showTypeToAdd = new ShowType(
        this.showType.type.trim(),
        this.showType.additionalPrice
      );

      this.showTypeService.add(showTypeToAdd).subscribe({
        next: (res) => {
          this.notificationService.successNotification("Show Type saved", res.message || "Show Type successfully saved!");
        },
        error: (err) => {
         if(err.status === 409) {
            this.notificationService.errorNotification("Conflict", err.error?.error);
            return;
          }
          this.notificationService.errorNotification("Error saving show type", "There was an error while saving the show type!");
          console.error(err);
        }
      })

    }
  }

}
