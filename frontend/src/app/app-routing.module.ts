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
import {AuthGuardService} from "./core/authentication/auth-guard.service";
import { ManageUsersComponent } from './modules/home/pages/manage-users/manage-users.component';

const routes: Routes = [
  { path: 'login',
    component: LoginComponent
  },

  { path: 'meinPortfolio',
    component: MyPortfolioComponent,
    canActivate:[AuthGuardService],
    data: {roles:['ADMIN', 'USER']}
  },

  { path: 'meinPortfolio/:wkn',
    component: ShowItemComponent,
    canActivate:[AuthGuardService],
    data: {roles:['ADMIN', 'USER']}
  },

  { path: 'pItemHinzufuegen',
    component: AddItemComponent,
    canActivate:[AuthGuardService],
    data: {roles:['ADMIN', 'USER']}
  },

  { path: 'legalNotice',
    component: LegalNoticeComponent
  },

  { path: 'privacyPolicy',
    component: PrivacyPolicyComponent
  },

  { path: 'missingPermission',
    component: MissingPermissionsComponent
  },

  { path: 'addAccount',
    component: AddAccountComponent,
    canActivate:[AuthGuardService],
    data: {roles:['ADMIN', 'USER']}
  },

  { path: 'benutzer',
    component: ManageUsersComponent,
    canActivate:[AuthGuardService],
    data: {roles:['ADMIN']}
  },

  // wildcard route
 // { path: '**', component: MyPortfolioComponent} // redirects any unspecified request to home page which is mein_portfolio
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
