import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyVisaViewComponent } from './apply-visa-view.component';

describe('ApplyVisaViewComponent', () => {
  let component: ApplyVisaViewComponent;
  let fixture: ComponentFixture<ApplyVisaViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplyVisaViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplyVisaViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
