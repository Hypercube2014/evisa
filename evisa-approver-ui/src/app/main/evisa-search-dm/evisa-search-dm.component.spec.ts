import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvisaSearchDmComponent } from './evisa-search-dm.component';

describe('EvisaSearchDmComponent', () => {
  let component: EvisaSearchDmComponent;
  let fixture: ComponentFixture<EvisaSearchDmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EvisaSearchDmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvisaSearchDmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
