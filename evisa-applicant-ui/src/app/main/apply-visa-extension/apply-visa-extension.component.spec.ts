import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyVisaExtensionComponent } from './apply-visa-extension.component';

describe('ApplyVisaExtensionComponent', () => {
  let component: ApplyVisaExtensionComponent;
  let fixture: ComponentFixture<ApplyVisaExtensionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplyVisaExtensionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplyVisaExtensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
