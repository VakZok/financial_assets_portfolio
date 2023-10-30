import { Component } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { PortfolioItemModel } from 'app/core/models/portfolio-item.model';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import { Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-show-item',
  templateUrl: './show-item.component.html',
  styleUrls: ['./show-item.component.css']
})
export class ShowItemComponent {
  data$: Observable<PortfolioItemModel>| null = null;
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router,) {
  }
  ngOnInit(): void {
    this.data$=this.route.paramMap.pipe(
      switchMap((params:ParamMap)=>this.pItemService.getPItem(params.get("id")!))
    )
  }
 
  goToPItem(id:string){
    this.router.navigate(['show_item',id])
  }
}

