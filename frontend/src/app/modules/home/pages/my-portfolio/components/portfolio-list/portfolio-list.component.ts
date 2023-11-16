import {Component, OnInit} from '@angular/core';
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {PurchaseDialogComponent} from "../purchase-dialog/purchase-dialog/purchase-dialog.component";
import {ShareModel} from "../../../../../../core/models/share.model";

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.css']
})
export class PortfolioListComponent implements OnInit{
  pItems:PortfolioItemModel[]=[];
  displayedColumns: string[] = ['wkn', 'name', 'totalQuantity', 'avgPrice', 'totalPrice', 'buy'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router, private dialog:MatDialog) {
  }
  // aggregated by wkn
  ngOnInit(): void {
    this.getData()
  }

  // get data for preview
  getData() {
    this.pItems = [] // instantiate pItems List
    this.pItemService.getPItemPreview().subscribe({
      next: (data) => {
        data.forEach( item => this.pItems.push(item)) // populate pItems List
        this.dataSource.data = this.pItems
      },
    })
  }

  goToPItem(wkn:string){
    this.router.navigate(['meinPortfolio',wkn])
  }

  openDialog(event:Event, shareDTO: ShareModel){
    event.stopPropagation();
    console.log(shareDTO)
    const dialogRef = this.dialog.open(PurchaseDialogComponent, {
      data: {
        shareDTO: shareDTO
        }
      }
    )
    // refresh data of Portfoliolist  after Purchase Dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.getData()
    })
  }
}
