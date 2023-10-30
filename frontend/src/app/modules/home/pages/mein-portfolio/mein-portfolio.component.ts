import { Component, OnInit} from '@angular/core';
import {DataSource} from '@angular/cdk/collections';
import {CdkTableModule} from '@angular/cdk/table';
import {BehaviorSubject, Observable} from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import { PortfolioItemModel } from 'app/core/models/portfolio-item.model';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
];


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
    this.pItemService.getPItemList().subscribe({
      next: (data) => { data.forEach( item => this.pItems.push(item))
        this.dataSource.data = this.pItems
      },  
    })}
  displayedColumns: string[] = ['id', 'wkn', 'name', 'purchasePrice', 'quantity'];
  dataSource = new MatTableDataSource<any>(this.pItems);
  goToPItem(id:string){
    this.router.navigate(['show_item',id])

  }
}

