import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExtensionEvisaBorderProcessComponent } from './extension-evisa-border-process.component';

describe('ExtensionEvisaBorderProcessComponent', () => {
  let component: ExtensionEvisaBorderProcessComponent;
  let fixture: ComponentFixture<ExtensionEvisaBorderProcessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExtensionEvisaBorderProcessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtensionEvisaBorderProcessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
