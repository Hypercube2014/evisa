import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferAgentComponent } from './transfer-agent.component';

describe('TransferAgentComponent', () => {
  let component: TransferAgentComponent;
  let fixture: ComponentFixture<TransferAgentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransferAgentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransferAgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
