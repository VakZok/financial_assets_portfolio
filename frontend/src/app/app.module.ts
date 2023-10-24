import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './core/header/header.component';
import { NavigationComponent } from './core/header/navigation/navigation.component';
import { FooterComponent } from './core/footer/footer.component';
import { AddItemComponent } from './modules/home/pages/add-item/add-item.component';
import { ItemInputFormComponent } from './modules/home/pages/add-item/components/item-input-form/item-input-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ShareInputFormComponent } from './modules/home/pages/add-share/components/share-input-form/share-input-form.component';
import {AddShareComponent} from "./modules/home/pages/add-share/add-share.component";
import {AppRoutingModule} from "./app-routing.module";
import {MeinPortfolioComponent} from "./modules/home/pages/mein-portfolio/mein-portfolio.component";
import {ImpressumComponent} from "./modules/home/pages/impressum/impressum.component";
import { DatenschutzComponent } from './modules/home/pages/datenschutz/datenschutz.component';


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
    ImpressumComponent,
    DatenschutzComponent
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
