import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisaViewComponent } from './visa-view.component';

describe('VisaViewComponent', () => {
  let component: VisaViewComponent;
  let fixture: ComponentFixture<VisaViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisaViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisaViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
