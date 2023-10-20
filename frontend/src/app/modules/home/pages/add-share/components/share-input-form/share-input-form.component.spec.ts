import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShareInputFormComponent } from './share-input-form.component';

describe('ShareInputFormComponent', () => {
  let component: ShareInputFormComponent;
  let fixture: ComponentFixture<ShareInputFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShareInputFormComponent]
    });
    fixture = TestBed.createComponent(ShareInputFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
