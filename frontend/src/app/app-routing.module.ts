import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { AddShareComponent} from "./modules/home/pages/add-share/add-share.component";
import { MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import { ShowItemComponent } from './modules/home/pages/show-item/show-item.component';
import { ImpressumComponent} from "./modules/home/pages/impressum/impressum.component";
import { DatenschutzComponent} from "./modules/home/pages/datenschutz/datenschutz.component";

const routes: Routes = [
  { path: '', redirectTo: '/mein_portfolio', pathMatch: 'full'}, // redirects to home page which is mein_portfolio
  { path: 'mein_portfolio', component: MeinPortfolioComponent},
  { path: 'show_item', component: ShowItemComponent},
  { path: 'asset_hinzufuegen', component: AddItemComponent},
  { path: 'share_hinzufuegen', component: AddShareComponent},
  { path: 'impressum', component: ImpressumComponent},
  { path: 'datenschutz', component: DatenschutzComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
