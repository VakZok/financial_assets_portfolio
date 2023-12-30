import {Component, OnInit} from '@angular/core';
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {PurchaseDialogComponent} from "../purchase-dialog/purchase-dialog/purchase-dialog.component";
import {ShareModel} from "../../../../../../core/models/share.model";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-portfolio-list',
  templateUrl: './portfolio-list.component.html',
  styleUrls: ['./portfolio-list.component.css']
})
export class PortfolioListComponent implements OnInit{

  pItems:PortfolioItemModel[]=[];
  displayedColumns: string[] = ['isin', 'name', 'totalQuantity', 'avgPrice', 'totalPrice', 'buy'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  favPressed = false

  constructor( private pItemService: PortfolioItemService, private route: ActivatedRoute, private router: Router, private dialog:MatDialog, private snackBar: MatSnackBar) {
  }
  // aggregated by isin
  ngOnInit(): void {
    if(this.router.url.includes('/meineFavoriten')){
      this.getFavData()
    }
    else if(this.router.url.includes('/meinPortfolio')){
      this.getData()
    }
  }

  get headerTitle() {
    if (this.router.url.includes('/meineFavoriten')) {
      return 'Meine Favoriten';
    }
    else {
      return 'Mein Portfolio';
    }
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

  // get data for favorites
  getFavData() {
    this.pItems = [] // instantiate pItems List
    this.pItemService.getLikedPItems().subscribe({
      next: (data) => {
        data.forEach( item => this.pItems.push(item)) // populate pItems List
        this.dataSource.data = this.pItems
      },
    })
  }

  goToPItem(isin:string){
    this.router.navigate(['meinPortfolio',isin])
  }

  openDialog(event:Event, shareDTO: ShareModel){
    event.stopPropagation();
    const dialogRef = this.dialog.open(PurchaseDialogComponent, {
      data: {
        shareDTO: shareDTO
        }
      })

    // refresh data of PItems List after Purchase Dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.getData()
    })
  }

  favoritePItem(event:Event, pItemDTO: PortfolioItemModel){
    event.stopPropagation();
    if(pItemDTO.isFavorite == true){
      this.pItemService.deleteLike(pItemDTO.shareDTO?.isin ||'').subscribe({
        next: () => {
          pItemDTO.isFavorite = false
          if (this.router.url.includes('/meineFavoriten')) {
            this.dataSource.data = this.dataSource.data.filter(pItem => pItem.isFavorite == true);
          }
        },
      })
    }

    else if(pItemDTO.isFavorite == false){
      this.pItemService.postLike(pItemDTO.shareDTO?.isin || '').subscribe({
        next: () => {
          this.openSnackBar(pItemDTO.shareDTO?.isin || '')
          pItemDTO.isFavorite = true
        }
      })

    }

  }
  // snackbar for success
  openSnackBar(isin:string) {
    this.snackBar.open('Favorit für "' + isin + '" hinzugefügt ❤', '', {
      duration: 3000
    });
  }
}
