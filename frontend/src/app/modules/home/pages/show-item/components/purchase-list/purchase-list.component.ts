import {Component, Input} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PurchaseModel} from "../../../../../../core/models/purchase.model";

@Component({
  selector: 'app-purchase-list',
  templateUrl: './purchase-list.component.html',
  styleUrls: ['./purchase-list.component.css']
})
export class PurchaseListComponent {
  displayedColumns: string[] = ['purchaseDate', 'quantity', 'purchasePrice', 'totalPrice'];
  @Input() dataSource = new MatTableDataSource<PurchaseModel>();
}
