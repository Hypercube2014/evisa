import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverstayDetailsComponent } from './overstay-details.component';

describe('OverstayDetailsComponent', () => {
  let component: OverstayDetailsComponent;
  let fixture: ComponentFixture<OverstayDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverstayDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverstayDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
