import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTypeInputComponent } from './show-type-input.component';

describe('ShowTypeInputComponent', () => {
  let component: ShowTypeInputComponent;
  let fixture: ComponentFixture<ShowTypeInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowTypeInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowTypeInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
