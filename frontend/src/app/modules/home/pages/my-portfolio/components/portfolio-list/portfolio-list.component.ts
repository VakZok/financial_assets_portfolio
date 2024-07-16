import {Component, OnInit} from '@angular/core';
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {Router} from "@angular/router";
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

  // This component allows the user to see a preview of his portfolio items or favorites.

  pItems:PortfolioItemModel[]=[];
  displayedColumns: string[] = ['isin', 'name', 'totalQuantity', 'avgPrice', 'totalPrice', 'profitAndLoss', 'buy'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  loading:boolean = false;
  profitLossLoaded:boolean = false;
  constructor( private pItemService: PortfolioItemService, private router: Router, private dialog:MatDialog, private snackBar: MatSnackBar) {
  }
  // aggregated by isin
  ngOnInit(): void {
    this.getData()
  }



  get headerTitle() {
    if (this.router.url.includes('/meineFavoriten')) {
      return 'Meine Favoriten';
    }
    else {
      return 'Mein Portfolio';
    }
  }

  get emptyDataMessage() {
    if (this.router.url.includes('/meineFavoriten')) {
      return 'Es sind noch keine Favoriten vorhanden. Portfolio-Items können in "Mein Portfolio" als Favoriten markiert werden.';
    }
    else {
      return 'Es sind noch keine Portfolio-Items vorhanden. Wählen Sie "Kaufen", um neue Portfolio-Items hinzuzufügen.';
    }
  }

  // get data for preview
  getData() {
    let favoritesOnly:boolean = false;
    if(this.router.url.includes('/meineFavoriten')){
      favoritesOnly = true;
    }

    this.loading = true;
    this.pItems = [] // instantiate pItems List
    this.pItemService.getPItemPreview(false, favoritesOnly).subscribe({
      next: (data) => {
        data.forEach( item => this.pItems.push(item)) // populate pItems List
        this.dataSource.data = this.pItems
        this.loading = false;
        this.pItemService.getPItemPreview(true, favoritesOnly).subscribe({
          next: (datPL: PortfolioItemModel[]) => {
            datPL.forEach(itemUp => {
              const index = this.dataSource.data.findIndex(itemOld => itemOld.shareDTO?.isin === itemUp.shareDTO?.isin)
              if(index!== -1){this.dataSource.data[index].profitAndLossCum = itemUp.profitAndLossCum}
              this.profitLossLoaded = true;
            }) // populate loaded profit and loss
          }
        })
      },
      error:() =>{
        this.loading = false;
      }
    })
  }

  goToPItem(isin:string){
    this.router.navigate(['meinPortfolio',isin])
  }

  openDialog(event:Event, shareDTO: ShareModel){
    event.stopPropagation();
    const dialogRef = this.dialog.open(PurchaseDialogComponent, {
      data: {shareDTO: shareDTO}
      })

    // refresh data of PItems List after Purchase Dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.profitLossLoaded = false;
      this.getData();
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
