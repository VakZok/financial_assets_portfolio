import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeinPortfolioComponent } from './mein-portfolio.component';

describe('MeinPortfolioComponent', () => {
  let component: MeinPortfolioComponent;
  let fixture: ComponentFixture<MeinPortfolioComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeinPortfolioComponent]
    });
    fixture = TestBed.createComponent(MeinPortfolioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
