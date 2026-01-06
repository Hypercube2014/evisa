import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchDepartureeVisaComponent } from './search-departuree-visa.component';

describe('SearchDepartureeVisaComponent', () => {
  let component: SearchDepartureeVisaComponent;
  let fixture: ComponentFixture<SearchDepartureeVisaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchDepartureeVisaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchDepartureeVisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
