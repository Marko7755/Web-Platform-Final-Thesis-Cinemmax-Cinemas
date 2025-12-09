import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTimeInputComponent } from './show-time-input.component';

describe('ShowTimeInputComponent', () => {
  let component: ShowTimeInputComponent;
  let fixture: ComponentFixture<ShowTimeInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowTimeInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTimeInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
