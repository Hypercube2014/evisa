import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardImoComponent } from './dashboard-imo.component';

describe('DashboardImoComponent', () => {
  let component: DashboardImoComponent;
  let fixture: ComponentFixture<DashboardImoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardImoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardImoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
