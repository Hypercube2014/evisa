import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeMaintenanceComponent } from './code-maintenance.component';

describe('CodeMaintenanceComponent', () => {
  let component: CodeMaintenanceComponent;
  let fixture: ComponentFixture<CodeMaintenanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CodeMaintenanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
