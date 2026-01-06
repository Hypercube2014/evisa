import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchExtensionVisaComponent } from './search-extension-visa.component';

describe('SearchExtensionVisaComponent', () => {
  let component: SearchExtensionVisaComponent;
  let fixture: ComponentFixture<SearchExtensionVisaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchExtensionVisaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchExtensionVisaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
