import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvisaBorderProcessingComponent } from './evisa-border-processing.component';

describe('EvisaBorderProcessingComponent', () => {
  let component: EvisaBorderProcessingComponent;
  let fixture: ComponentFixture<EvisaBorderProcessingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EvisaBorderProcessingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvisaBorderProcessingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
