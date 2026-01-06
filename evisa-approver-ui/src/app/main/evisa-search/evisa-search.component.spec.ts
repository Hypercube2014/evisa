import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvisaSearchComponent } from './evisa-search.component';

describe('EvisaSearchComponent', () => {
  let component: EvisaSearchComponent;
  let fixture: ComponentFixture<EvisaSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EvisaSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvisaSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
