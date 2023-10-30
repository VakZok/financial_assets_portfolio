import { Component, OnInit} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import { PortfolioItemModel } from 'app/core/models/portfolio-item.model';


@Component({
  selector: 'app-mein-portfolio',
  templateUrl: './mein-portfolio.component.html',
  styleUrls: ['./mein-portfolio.component.css']
})

export class MeinPortfolioComponent implements OnInit {
  pItems:PortfolioItemModel[]=[];
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router,) {
  }
  ngOnInit(): void {
    this.pItemService.getPItemPrevList().subscribe({
      next: (data) => {
        console.log(data)
        data.forEach( item => this.pItems.push(item))
        this.dataSource.data = this.pItems
        console.log(this.pItems)
      },
    })
  }

  displayedColumns: string[] = ['id', 'wkn', 'name', 'purchasePrice', 'quantity'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  goToPItem(id:string){
    this.router.navigate(['meinPortfolio',id])
  }
}

