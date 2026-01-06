import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisaoverstayViewComponent } from './visaoverstay-view.component';

describe('VisaoverstayViewComponent', () => {
  let component: VisaoverstayViewComponent;
  let fixture: ComponentFixture<VisaoverstayViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisaoverstayViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisaoverstayViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
