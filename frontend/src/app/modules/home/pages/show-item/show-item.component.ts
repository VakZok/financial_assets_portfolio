import {Component, OnInit} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { PortfolioItemModel } from 'app/core/models/portfolio-item.model';
import { PurchaseModel } from 'app/core/models/purchase.model';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import {Observable, switchMap, take} from 'rxjs';

@Component({
  selector: 'app-show-item',
  templateUrl: './show-item.component.html',
  styleUrls: ['./show-item.component.css']
})
export class ShowItemComponent implements OnInit {
  data$: Observable<PortfolioItemModel>| null = null;
  loading: boolean = false;
  dataSource = new MatTableDataSource<PurchaseModel>();
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute) {}

  // get Data of PItem
  ngOnInit(): void {
    this.data$ = this.route.paramMap.pipe(
      switchMap((params:ParamMap)=>this.pItemService.getPItemByISIN(params.get("isin")!))
    );
    this.data$.pipe(take(1)).subscribe(data=>{
      if(data.purchaseDTOList){
        this.dataSource.data = data.purchaseDTOList;
        this.loading = true;
      }
    })
  }
}

