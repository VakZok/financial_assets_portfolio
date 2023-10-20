import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemInputFormComponent } from './item-input-form.component';

describe('ItemInputFormComponent', () => {
  let component: ItemInputFormComponent;
  let fixture: ComponentFixture<ItemInputFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemInputFormComponent]
    });
    fixture = TestBed.createComponent(ItemInputFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
