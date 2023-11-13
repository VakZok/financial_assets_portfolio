import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyPortfolioComponent } from './my-portfolio.component';

describe('MeinPortfolioComponent', () => {
  let component: MyPortfolioComponent;
  let fixture: ComponentFixture<MyPortfolioComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyPortfolioComponent]
    });
    fixture = TestBed.createComponent(MyPortfolioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
