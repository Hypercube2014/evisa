import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentCompletedEditComponent } from './payment-completed-edit.component';

describe('PaymentCompletedEditComponent', () => {
  let component: PaymentCompletedEditComponent;
  let fixture: ComponentFixture<PaymentCompletedEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentCompletedEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentCompletedEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
