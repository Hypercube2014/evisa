import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplyVisaListComponent } from './apply-visa-list.component';

describe('ApplyVisaListComponent', () => {
  let component: ApplyVisaListComponent;
  let fixture: ComponentFixture<ApplyVisaListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApplyVisaListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplyVisaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
