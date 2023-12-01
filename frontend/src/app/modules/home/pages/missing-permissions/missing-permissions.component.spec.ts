import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MissingPermissionsComponent } from './missing-permissions.component';

describe('MissingPermissionsComponent', () => {
  let component: MissingPermissionsComponent;
  let fixture: ComponentFixture<MissingPermissionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MissingPermissionsComponent]
    });
    fixture = TestBed.createComponent(MissingPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
