import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { HeaderComponent } from './header/header.component';
import { NavigationComponent } from './navigation/navigation.component';
import { FooterComponent } from './footer/footer.component';
import { AddItemComponent } from './modules/home/pages/add-item/add-item.component';
import { ItemInputFormComponent } from './modules/home/pages/add-item/components/item-input-form/item-input-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ShareInputFormComponent } from './modules/home/pages/add-share/components/share-input-form/share-input-form.component';
import {AddShareComponent} from "./modules/home/pages/add-share/add-share.component";


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

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
