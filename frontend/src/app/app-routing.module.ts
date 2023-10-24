import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { AddShareComponent} from "./modules/home/pages/add-share/add-share.component";
import { MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import { ImpressumComponent} from "./modules/home/pages/impressum/impressum.component";

const routes: Routes = [
  { path: '', redirectTo: '/mein_portfolio', pathMatch: 'full'}, // redirects to home page which is mein_portfolio
  { path: 'mein_portfolio', component: MeinPortfolioComponent},
  { path: 'asset_hinzufuegen', component: AddItemComponent},
  { path: 'add_share', component: AddShareComponent},
  { path: 'impressum', component: ImpressumComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
