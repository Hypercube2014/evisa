import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuspendAgentComponent } from './suspend-agent.component';

describe('SuspendAgentComponent', () => {
  let component: SuspendAgentComponent;
  let fixture: ComponentFixture<SuspendAgentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuspendAgentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SuspendAgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
