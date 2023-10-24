import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { HeaderComponent } from './core/header/header.component';
import { NavigationComponent } from './navigation/navigation.component';
import { FooterComponent } from './core/footer/footer.component';
import { AddItemComponent } from './modules/home/pages/add-item/add-item.component';
import { ItemInputFormComponent } from './modules/home/pages/add-item/components/item-input-form/item-input-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ShareInputFormComponent } from './modules/home/pages/add-share/components/share-input-form/share-input-form.component';
import {AddShareComponent} from "./modules/home/pages/add-share/add-share.component";
import { MeinPortfolioComponent } from './modules/home/pages/mein-portfolio/mein-portfolio.component';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    NavigationComponent,
    FooterComponent,
    AddItemComponent,
    ItemInputFormComponent,
    ShareInputFormComponent,
    AddShareComponent,
    MeinPortfolioComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
