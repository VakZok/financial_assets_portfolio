import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { MyPortfolioComponent} from "./modules/home/pages/my-portfolio/my-portfolio.component";
import { ShowItemComponent } from './modules/home/pages/show-item/show-item.component';
import { LegalNoticeComponent} from "./modules/home/pages/legal-notice/legal-notice.component";
import { PrivacyPolicyComponent} from "./modules/home/pages/privacy-policy/privacy-policy.component";
import {LoginComponent} from "./modules/home/pages/login/login.component";
import {MissingPermissionsComponent} from "./modules/home/pages/missing-permissions/missing-permissions.component";
import {AddAccountComponent} from "./modules/home/pages/add-account/add-account.component";

const routes: Routes = [
  { path: 'meinPortfolio', component: MyPortfolioComponent},
  { path: 'meinPortfolio/:wkn', component: ShowItemComponent},
  { path: 'pItemHinzufuegen', component: AddItemComponent},
  { path: 'legalNotice', component: LegalNoticeComponent},
  { path: 'privacyPolicy', component: PrivacyPolicyComponent},
  { path: 'login', component: LoginComponent},
  { path: 'missingPermission', component: MissingPermissionsComponent},
  { path: 'addAccount', component: AddAccountComponent},
  // wildcard route
  { path: '**', component: MyPortfolioComponent} // redirects any unspecified request to home page which is mein_portfolio
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
