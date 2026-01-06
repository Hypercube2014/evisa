import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisaOverstayComponent } from './visa-overstay.component';

describe('VisaOverstayComponent', () => {
  let component: VisaOverstayComponent;
  let fixture: ComponentFixture<VisaOverstayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisaOverstayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisaOverstayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
