import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from "@angular/forms";

export abstract class CinemaFormBase {
    cinemaForm: FormGroup;
    constructor(public fb: FormBuilder) {
        this.cinemaForm = this.fb.group({
            name: [
                '',
                [
                    Validators.required,
                    this.noWhitespaceOnly,
                    Validators.pattern(/^[A-Za-z\s]+$/)
                ]
            ],
            location: [
                '',
                [
                    Validators.required,
                    this.noWhitespaceOnly,
                    Validators.pattern(/^[A-Za-z\s]+$/)
                ]
            ]
        });
    }

    // Custom validator to prevent whitespace-only input
    noWhitespaceOnly(control: AbstractControl): ValidationErrors | null {
        const value = (control.value || '').trim();
        return value.length === 0 ? { whitespace: true } : null;
    }
}
