import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatInputComponent } from './seat-input.component';

describe('SeatInputComponent', () => {
  let component: SeatInputComponent;
  let fixture: ComponentFixture<SeatInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeatInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeatInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
