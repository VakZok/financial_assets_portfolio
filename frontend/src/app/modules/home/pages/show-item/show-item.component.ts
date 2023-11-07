import { Component } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { AggPortfolioItemModel } from 'app/core/models/agg-portfolio-item.model';
import { PortfolioItemModel } from 'app/core/models/portfolio-item.model';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import { Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-show-item',
  templateUrl: './show-item.component.html',
  styleUrls: ['./show-item.component.css']
})
export class ShowItemComponent {
  data$: Observable<AggPortfolioItemModel>| null = null;
  displayedColumns: string[] = ['purchaseDate', 'quantity', 'purchasePrice', 'totalPrice'];
  dataSource = new MatTableDataSource<PortfolioItemModel>();
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router,) {
  }
  ngOnInit(): void {
    this.data$=this.route.paramMap.pipe(
      switchMap((params:ParamMap)=>this.pItemService.getWKNAggPItem(params.get("wkn")!))
    )
    this.data$.subscribe(data=>{
      if(data.pItemDTOList){
        this.dataSource.data=data.pItemDTOList;
      }      
    })
  }
  

}

