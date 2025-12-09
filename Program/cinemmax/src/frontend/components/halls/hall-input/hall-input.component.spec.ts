import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HallInputComponent } from './hall-input.component';

describe('HallInputComponent', () => {
  let component: HallInputComponent;
  let fixture: ComponentFixture<HallInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HallInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HallInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
