import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageVisaComponent } from './manage-visa.component';

describe('VisaDetailsComponent', () => {
  let component: ManageVisaComponent;
  let fixture: ComponentFixture<ManageVisaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageVisaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageVisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
