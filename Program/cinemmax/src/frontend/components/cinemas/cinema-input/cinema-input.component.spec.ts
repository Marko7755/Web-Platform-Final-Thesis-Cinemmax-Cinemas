import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CinemaInputComponent } from './cinema-input.component';

describe('CinemaInputComponent', () => {
  let component: CinemaInputComponent;
  let fixture: ComponentFixture<CinemaInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CinemaInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CinemaInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
