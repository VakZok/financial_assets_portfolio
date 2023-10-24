import { RouterModule, Routes } from '@angular/router';

import { NgModule } from '@angular/core';


import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { AddShareComponent} from "./modules/home/pages/add-share/add-share.component";

const routes: Routes = [
  { path: '', redirectTo: '/asset_hinzuefuegen', pathMatch: 'full'}, // redirects to home page which is mein_portfolio
  { path: 'asset_hinzufuegen', component: AddItemComponent},
  { path: 'add_share', component: AddShareComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
