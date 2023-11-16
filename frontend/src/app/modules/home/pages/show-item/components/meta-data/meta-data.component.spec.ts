import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetaDataComponent } from './meta-data.component';

describe('MetaDataComponent', () => {
  let component: MetaDataComponent;
  let fixture: ComponentFixture<MetaDataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MetaDataComponent]
    });
    fixture = TestBed.createComponent(MetaDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
