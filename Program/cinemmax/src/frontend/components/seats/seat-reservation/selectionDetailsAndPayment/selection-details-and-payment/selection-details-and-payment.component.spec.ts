import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectionDetailsAndPaymentComponent } from './selection-details-and-payment.component';

describe('SelectionDetailsAndPaymentComponent', () => {
  let component: SelectionDetailsAndPaymentComponent;
  let fixture: ComponentFixture<SelectionDetailsAndPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectionDetailsAndPaymentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectionDetailsAndPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
