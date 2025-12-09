import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CinemaManageComponent } from './cinema-manage.component';

describe('CinemaManageComponent', () => {
  let component: CinemaManageComponent;
  let fixture: ComponentFixture<CinemaManageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CinemaManageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CinemaManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
