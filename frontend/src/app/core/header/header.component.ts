import { Component } from '@angular/core';
import {AuthCoreService} from "../authentication/auth-core.service";


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(public authService: AuthCoreService){}

}
