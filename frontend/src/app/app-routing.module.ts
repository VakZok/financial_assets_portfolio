import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { AddShareComponent} from "./modules/home/pages/add-share/add-share.component";
import { MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import { ImpressumComponent} from "./modules/home/pages/impressum/impressum.component";
import { DatenschutzComponent} from "./modules/home/pages/datenschutz/datenschutz.component";

const routes: Routes = [
  { path: 'mein_portfolio', component: MeinPortfolioComponent},
  { path: 'asset_hinzufuegen', component: AddItemComponent},
  { path: 'share_hinzufuegen', component: AddShareComponent},
  { path: 'impressum', component: ImpressumComponent},
  { path: 'datenschutz', component: DatenschutzComponent},
  // wildcard route
  { path: '**', component: MeinPortfolioComponent} // redirects any unspecified request to home page which is mein_portfolio
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
