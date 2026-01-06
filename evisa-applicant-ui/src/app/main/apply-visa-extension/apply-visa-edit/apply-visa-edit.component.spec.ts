import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyVisaEditComponent } from './apply-visa-edit.component';

describe('ApplyVisaEditComponent', () => {
  let component: ApplyVisaEditComponent;
  let fixture: ComponentFixture<ApplyVisaEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplyVisaEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplyVisaEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
