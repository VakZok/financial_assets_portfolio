import { BrowserModule } from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './core/header/header.component';
import { NavigationComponent } from './core/header/navigation/navigation.component';
import { FooterComponent } from './core/footer/footer.component';
import { AddItemComponent } from './modules/home/pages/add-item/add-item.component';
import { ItemInputFormComponent } from './modules/home/pages/add-item/components/item-input-form/item-input-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app-routing.module";
import {MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import { ShowItemComponent } from './modules/home/pages/show-item/show-item.component';
import {ImpressumComponent} from "./modules/home/pages/impressum/impressum.component";
import { DatenschutzComponent } from './modules/home/pages/datenschutz/datenschutz.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from "@angular/material/table";
import {CurrencyPipe, registerLocaleData} from '@angular/common';
import * as de from '@angular/common/locales/de';
import {MatInputModule} from "@angular/material/input";
import {MatCardModule} from "@angular/material/card";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    NavigationComponent,
    FooterComponent,
    AddItemComponent,
    ItemInputFormComponent,
    MeinPortfolioComponent,
    ShowItemComponent,
    ImpressumComponent,
    DatenschutzComponent
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
    MatCardModule
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
