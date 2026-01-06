import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentTrackerComponent } from './agent-tracker.component';

describe('AgentTrackerComponent', () => {
  let component: AgentTrackerComponent;
  let fixture: ComponentFixture<AgentTrackerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgentTrackerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentTrackerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
