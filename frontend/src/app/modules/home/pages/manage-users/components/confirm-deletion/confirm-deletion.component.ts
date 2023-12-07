import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserManagementService } from 'app/core/services/user-management.service';
import { UserDialogComponent } from '../user-dialog/user-dialog.component';

@Component({
  selector: 'app-confirm-deletion',
  templateUrl: './confirm-deletion.component.html',
  styleUrls: ['./confirm-deletion.component.css']
})
export class ConfirmDeletionComponent {
  accountUsername:string;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<UserDialogComponent>,
    public accountService: UserManagementService,
    ){
    this.accountUsername = data.accountDTO.username;
  }

  delete(){
    this.accountService.deleteAccount(this.accountUsername).subscribe({
      next: () => {
        this.dialogRef.close()
      },
      error: (errors) => errors.error.forEach((item: any) => {
      }),
    })
  }
}
