import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { AddShareComponent} from "./modules/home/pages/add-share/add-share.component";

const routes: Routes = [
  { path: '', redirectTo: '/mein_portfolio', pathMatch: 'full'}, // redirects to home page which is mein_portfolio
  { path: 'mein_portfolio', component: MeinPortfolioComponent, title: 'Mein Portfolio'},
  { path: 'asset_hinzufuegen', component: AddShareComponent, title: 'Asset hinzufuegen'},
  { path: 'asset_hinzufuegen', component: AddItemComponent,  title: 'Asset hinzufuegen'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
