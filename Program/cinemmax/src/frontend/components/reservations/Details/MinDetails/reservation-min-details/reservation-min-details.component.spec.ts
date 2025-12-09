import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationMinDetailsComponent } from './reservation-min-details.component';

describe('ReservationMinDetailsComponent', () => {
  let component: ReservationMinDetailsComponent;
  let fixture: ComponentFixture<ReservationMinDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationMinDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationMinDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
