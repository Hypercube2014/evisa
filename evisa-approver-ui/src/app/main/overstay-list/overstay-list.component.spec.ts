import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverstayListComponent } from './overstay-list.component';

describe('OverstayListComponent', () => {
  let component: OverstayListComponent;
  let fixture: ComponentFixture<OverstayListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverstayListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverstayListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
