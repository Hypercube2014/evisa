import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvisaSearchApplicationComponent } from './evisa-search-application.component';

describe('EvisaSearchApplicationComponent', () => {
  let component: EvisaSearchApplicationComponent;
  let fixture: ComponentFixture<EvisaSearchApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EvisaSearchApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvisaSearchApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
