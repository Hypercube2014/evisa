import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExitBcoComponent } from './exit-bco.component';

describe('ExitBcoComponent', () => {
  let component: ExitBcoComponent;
  let fixture: ComponentFixture<ExitBcoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExitBcoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExitBcoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
