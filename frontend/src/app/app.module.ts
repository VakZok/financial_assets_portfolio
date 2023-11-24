import { BrowserModule } from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './core/header/header.component';
import { FooterComponent } from './core/footer/footer.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app-routing.module";
import {MyPortfolioComponent} from "./modules/home/pages/my-portfolio/my-portfolio.component";
import { ShowItemComponent } from './modules/home/pages/show-item/show-item.component';
import {LegalNoticeComponent} from "./modules/home/pages/legal-notice/legal-notice.component";
import { PrivacyPolicyComponent } from './modules/home/pages/privacy-policy/privacy-policy.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from "@angular/material/table";
import {CurrencyPipe, registerLocaleData} from '@angular/common';
import * as de from '@angular/common/locales/de';
import {MatInputModule} from "@angular/material/input";
import {MatCardModule} from "@angular/material/card";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatButtonModule} from "@angular/material/button";
import {AddItemComponent} from "./modules/home/pages/add-item/add-item.component";
import { InputFormComponent } from './modules/home/pages/add-item/components/input-form/input-form.component';
import { PortfolioListComponent } from './modules/home/pages/my-portfolio/components/portfolio-list/portfolio-list.component';
import { PurchaseListComponent } from './modules/home/pages/show-item/components/purchase-list/purchase-list.component';
import { MetaDataComponent } from './modules/home/pages/show-item/components/meta-data/meta-data.component';
import { PurchaseDialogComponent } from './modules/home/pages/my-portfolio/components/purchase-dialog/purchase-dialog/purchase-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { NavigationBarComponent } from './core/header/components/navigation-bar/navigation-bar.component';
import { LoginComponent } from './modules/home/pages/login/login.component';
import { MissingPermissionsComponent } from './modules/home/pages/missing-permissions/missing-permissions.component';
import { AddAccountComponent } from './modules/home/pages/add-account/add-account.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    AddItemComponent,
    MyPortfolioComponent,
    ShowItemComponent,
    LegalNoticeComponent,
    PrivacyPolicyComponent,
    InputFormComponent,
    PortfolioListComponent,
    PurchaseListComponent,
    MetaDataComponent,
    PurchaseDialogComponent,
    NavigationBarComponent,
    LoginComponent,
    MissingPermissionsComponent,
    AddAccountComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatInputModule,
    MatCardModule,
    MatSnackBarModule,
    MatButtonModule,
    MatDialogModule
  ],
  providers: [{
    provide: LOCALE_ID,
    useValue: 'de-DE' // 'de-DE' for Germany, 'fr-FR' for France ...

  },
  CurrencyPipe],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(de.default);
  }
}
