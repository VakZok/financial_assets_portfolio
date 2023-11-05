import { Component, OnInit} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import {AggPortfolioItemModel} from "../../../../core/models/agg-portfolio-item.model";


@Component({
  selector: 'app-mein-portfolio',
  templateUrl: './mein-portfolio.component.html',
  styleUrls: ['./mein-portfolio.component.css']
})

export class MeinPortfolioComponent implements OnInit {
  pItems:AggPortfolioItemModel[]=[];
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router,) {
  }
  // aggregated by wkn
  ngOnInit(): void {
    this.pItemService.getWKNAggPItemList().subscribe({
      next: (data) => {
        data.forEach( item => this.pItems.push(item))
        this.dataSource.data = this.pItems
      },
    })
  }

  displayedColumns: string[] = ['wkn', 'name', 'totalQuantity', 'avgPrice', 'buy'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  goToPItem(wkn:string){
    this.router.navigate(['meinPortfolio',wkn])
  }
}

