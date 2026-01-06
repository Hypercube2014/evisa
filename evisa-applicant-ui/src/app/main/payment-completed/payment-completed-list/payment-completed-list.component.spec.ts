import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentCompletedListComponent } from './payment-completed-list.component';

describe('PaymentCompletedListComponent', () => {
  let component: PaymentCompletedListComponent;
  let fixture: ComponentFixture<PaymentCompletedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentCompletedListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentCompletedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
