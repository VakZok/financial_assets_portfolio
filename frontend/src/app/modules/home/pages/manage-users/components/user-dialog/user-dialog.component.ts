import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { AccountModel } from 'app/core/models/account.model';
import {FormControl, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import { UserManagementService } from 'app/core/services/user-management.service';
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent implements OnInit{

  /*
  This component is used to create and edit users.
  It realizes validation of the input fields and displayes
  custom error messages, if the input is not valid.
   */

  @ViewChild(FormGroupDirective) form: any;
  accountForm = new FormGroup({
    username: new FormControl('',[
      Validators.maxLength(30)]),
    name: new FormControl('', [
      Validators.maxLength(30)
    ]),
    password: new FormControl('', [
      Validators.maxLength(30)
    ]),
    confirmPassword: new FormControl('', [
      Validators.maxLength(30)
    ]),
    role: new FormControl('', [
      Validators.required,
    ])
  });

  accountDTO: AccountModel = {
    username: '',
    name: '',
    password: '',
    role: ''
  }
  initUsername = ''
  errorMap = new Map<string, string>([
    ['username', ''],
    ['name', ''],
    ['password', ''],
    ['confirmPassword', ''],
    ['role', '']
  ]);

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<UserDialogComponent>,
    public userManService: UserManagementService,
    private snackBar: MatSnackBar){
    this.accountDTO = data.accountDTO;
  }

  ngOnInit(){
    this.initUsername = this.accountDTO.username;
    this.accountForm.controls['name'].setValue(this.accountDTO.name);
    this.accountForm.controls['username'].setValue(this.accountDTO.username);
    this.accountForm.controls['role'].setValue(this.accountDTO.role);
  }

  onSubmit() {
    this.accountForm.controls['password'].updateValueAndValidity()
    this.accountForm.controls['confirmPassword'].updateValueAndValidity()

    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.accountForm.controls) {
      if (this.accountForm.controls.hasOwnProperty(controlName)
        && (controlName !== 'password') && (controlName !== 'confirmPassword')) {
        const control = this.accountForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }

    // map  validation errors
    if (this.accountForm.controls['name'].errors?.['maxLength']) {
      this.errorMap.set('name', 'Der Name darf nicht länger als 30 Zeichen sein');
    } else {
      this.errorMap.set('name', '');
    }

    if (this.accountForm.controls['username'].errors?.['maxLength']) {
      this.errorMap.set('username', 'Der Benutzername darf nicht länger als 30 Zeichen sein');
    } else {
      this.errorMap.set('username', '');
    }

    if (this.accountForm.controls['password'].errors?.['maxLength']) {
      this.errorMap.set('password', 'Das Passwort darf nicht länger als 30 Zeichen sein');
    } else if (this.accountForm.controls['password'].errors?.['passwordMismatch']) {
      this.errorMap.set('password', 'Passwörter stimmen nicht überein');
    } else if (this.accountForm.controls['password']?.value !== this.accountForm.controls['confirmPassword']?.value) {
      this.accountForm.controls['password'].setErrors(['passwordMismatch'])
      this.accountForm.controls['confirmPassword'].setErrors(['passwordMismatch'])
      this.errorMap.set('password', 'Passwörter stimmen nicht überein');
      this.errorMap.set('confirmPassword', 'Passwörter stimmen nicht überein');
    } else {
      this.errorMap.set('password', '');
    }

    if (this.accountForm.controls['confirmPassword'].errors?.['required']) {
      this.errorMap.set('confirmPassword', 'Bitte bestätigen sie das Passwort');
    } else if (this.accountForm.controls['password']?.value !== this.accountForm.controls['confirmPassword']?.value) {
      this.accountForm.controls['password'].setErrors(['passwordMismatch'])
      this.accountForm.controls['confirmPassword'].setErrors(['passwordMismatch'])
      this.errorMap.set('password', 'Passwörter stimmen nicht überein');
      this.errorMap.set('confirmPassword', 'Passwörter stimmen nicht überein');
    } else {
      this.errorMap.set('confirmPassword', '');
    }

    if (this.accountForm.valid) {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }

      //create accountDTO
      const accountDTO: AccountModel = {
        name: (this.accountForm.controls['name'].value || ''),
        username: (this.accountForm.controls['username'].value || ''),
        password: (this.accountForm.controls['password'].value || ''),
        role: (this.accountForm.controls['role'].value || '')
      }

      // put accountDTO
      this.userManService.putAccount(this.initUsername, accountDTO).subscribe({
        next: (data) => {
          this.dialogRef.close()
          this.openSnackBar(this.accountDTO.username!)
        },

        error: (errors) => errors.error.forEach((item: any) => {
          this.accountForm.get(item.name)?.setErrors(item.message)
          this.errorMap.set(item.name, item.message);
        }),
      })
    }
  }

  // snackbar for success
  openSnackBar(username:string) {
    this.snackBar.open('Benutzer "' + username + '" wurde erfolgreich geändert ✔️', '', {
      duration: 3000
    });
  }



}
