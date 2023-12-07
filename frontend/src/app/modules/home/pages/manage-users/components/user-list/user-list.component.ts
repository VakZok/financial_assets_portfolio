import { Component, OnInit } from '@angular/core';
import { AccountModel } from 'app/core/models/account.model';
import { UserManagementService } from 'app/core/services/user-management.service';
import {ActivatedRoute, Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import { UserDialogComponent } from '../user-dialog/user-dialog.component';
import { ConfirmDeletionComponent } from '../confirm-deletion/confirm-deletion.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit{
  accounts:AccountModel[]=[];
  displayedColumns: string[] = ['name', 'username', 'password', 'role', 'edituser', 'deleteuser'];
  dataSource = new MatTableDataSource<any>(this.accounts);

  constructor( private accountService: UserManagementService, private route: ActivatedRoute, private router: Router, private dialog:MatDialog) {
  }

  ngOnInit(): void {
    this.getData()
  }

  // get data for preview
  getData() {
    this.accounts = [] // instantiate accounts List
    this.accountService.getAccountPreview().subscribe({
      next: (data) => {
        data.forEach( item => this.accounts.push(item)) // populate accounts List
        this.dataSource.data = this.accounts
      },
    })
  }

  /*goToAccount(name:string){
    this.router.navigate(['benutzer',name])
  } */

  openDialog(event:Event, accountDTO: AccountModel){
    console.log(accountDTO);
    console.log("test");
    event.stopPropagation();
    const dialogRef = this.dialog.open(UserDialogComponent, {
      data: {
        accountDTO: accountDTO
        }
      })


    // refresh data of accounts List after User Dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.getData()
    })
  }

  openDialogDelete(event:Event, accountDTO: AccountModel){
    console.log(accountDTO);
    console.log("test");
    event.stopPropagation();
    const dialogRef = this.dialog.open(ConfirmDeletionComponent, {
      data: {
        accountDTO: accountDTO
        }
    })   


    // refresh data of accounts List after User Dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.getData()
    })
  }

}
  

