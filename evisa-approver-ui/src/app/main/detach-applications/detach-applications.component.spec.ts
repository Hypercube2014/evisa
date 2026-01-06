import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetachApplicationsComponent } from './detach-applications.component';

describe('DetachApplicationsComponent', () => {
  let component: DetachApplicationsComponent;
  let fixture: ComponentFixture<DetachApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetachApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetachApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
