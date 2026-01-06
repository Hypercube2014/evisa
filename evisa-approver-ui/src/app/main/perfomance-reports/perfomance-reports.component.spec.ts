import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfomanceReportsComponent } from './perfomance-reports.component';

describe('PerfomanceReportsComponent', () => {
  let component: PerfomanceReportsComponent;
  let fixture: ComponentFixture<PerfomanceReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerfomanceReportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PerfomanceReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
