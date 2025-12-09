import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BestRatedComponent } from './best-rated.component';

describe('BestRatedComponent', () => {
  let component: BestRatedComponent;
  let fixture: ComponentFixture<BestRatedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BestRatedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BestRatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
