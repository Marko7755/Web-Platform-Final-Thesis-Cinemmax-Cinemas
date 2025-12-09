import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationFullDetailsComponent } from './reservation-full-details.component';

describe('ReservationFullDetailsComponent', () => {
  let component: ReservationFullDetailsComponent;
  let fixture: ComponentFixture<ReservationFullDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationFullDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationFullDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
