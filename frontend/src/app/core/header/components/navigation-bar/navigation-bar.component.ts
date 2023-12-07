import { Component } from '@angular/core';
import {Router, Event, RouterEvent} from "@angular/router";
import {debounceTime, filter, take} from "rxjs";
import {AuthCoreService} from "../../../authentication/auth-core.service";

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent {


  currentPage:string = "";
  navMapDisplay = new Map<string, boolean>([
    ['/meinPortfolio', false],
    ['/benutzer', false],
  ]);
  constructor(private router: Router, public authService: AuthCoreService) {
    router.events.pipe(
      filter((e: Event | RouterEvent): e is RouterEvent => e instanceof RouterEvent),
      debounceTime(5)
    ).subscribe((e: RouterEvent) => {
      this.currentPage = e.url
      console.log(this.currentPage)
    });
  }


  public logout(){
    this.authService.logout()
  }

}
