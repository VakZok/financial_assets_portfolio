import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserManagementService } from 'app/core/services/user-management.service';
import { UserDialogComponent } from '../user-dialog/user-dialog.component';
import {MatSnackBar} from "@angular/material/snack-bar";

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
    private snackBar: MatSnackBar
    ){
    this.accountUsername = data.accountDTO.username;
  }

  delete(){
    this.accountService.deleteAccount(this.accountUsername).subscribe({
      next: () => {
        this.dialogRef.close()
        this.openSnackBar(this.accountUsername!)
      },
      error: (errors) => errors.error.forEach((item: any) => {
      }),
    })
  }

  // snackbar for success
  openSnackBar(username:string) {
    this.snackBar.open('Benutzer "' + username + '" wurde gelöscht ✔️', '', {
      duration: 3000
    });
  }
}
