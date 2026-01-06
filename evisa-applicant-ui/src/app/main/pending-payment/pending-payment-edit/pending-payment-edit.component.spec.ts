import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingPaymentEditComponent } from './pending-payment-edit.component';

describe('PendingPaymentEditComponent', () => {
  let component: PendingPaymentEditComponent;
  let fixture: ComponentFixture<PendingPaymentEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PendingPaymentEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingPaymentEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
