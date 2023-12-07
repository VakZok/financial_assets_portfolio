import { Component, Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { AccountModel } from 'app/core/models/account.model';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { UserManagementService } from 'app/core/services/user-management.service';
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent implements OnInit{
  public userForm: FormGroup<{ 
    name: FormControl<string | null>; 
    username: FormControl<string | null>;
    password: FormControl<string | null>;
    confirmPassword: FormControl<string | null>;  
    role: FormControl<string | null>}>;

  accountDTO: AccountModel = {
    username: '',
    name: '',
    password: '',
    role: ''
  }
  errorMap = new Map<string, string>([
    ['name', ''],
    ['username', ''],
    ['password', ''],
    ['role', ''],
  ]);

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<UserDialogComponent>, 
    public accountService: UserManagementService, private router: Router,
    private snackBar: MatSnackBar){

    this.accountDTO = data.accountDTO;
    this.userForm = new FormGroup({
      name: new FormControl('', {
        validators:[
          Validators.max(255)],
        updateOn: 'submit'}),
      username: new FormControl('', {
        validators: [
          Validators.max(255)],
        updateOn:'submit'}),
      password: new FormControl('', {
        validators: [
          Validators.max(255)],
        updateOn:'submit'}),
      confirmPassword: new FormControl('', {
        validators: [
          Validators.max(255)],
        updateOn:'submit'}),
      role: new FormControl('', {
        validators: [
          Validators.max(255)],
        updateOn:'submit'})
  })
  }
  // function to prevent other characters than digits
  onInputName(event: Event) {
    const inputElement = event.target as HTMLInputElement;
  }

  onInputUsername(event: Event) {
    const inputElement = event.target as HTMLInputElement;
  }

  onInputPassword(event: Event) {
    const inputElement = event.target as HTMLInputElement;
  }

  onInputRole(event: Event) {
    const inputElement = event.target as HTMLInputElement;
  }

  ngOnInit(){
    this.userForm.controls['name'].setValue(this.accountDTO.name);
    this.userForm.controls['username'].setValue(this.accountDTO.username);
    this.userForm.controls['role'].setValue(this.accountDTO.role);
    console.log(this.userForm.controls['name'].value);
  }



  onSubmit() {

    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.userForm.controls) {
      if (this.userForm.controls.hasOwnProperty(controlName)) {
        const control = this.userForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }
    // map  validation errors
    if (this.userForm.controls['name'].errors?.['required']) {
      this.errorMap.set('name', 'Bitte tragen Sie einen neuen Namen ein');   
    } else {
      this.errorMap.set('name', '');
    }

    if (this.userForm.controls['username'].errors?.['required']) {
      this.errorMap.set('username', 'Bitte tragen Sie einen neuen Benutzernamen ein');
    } else {
      this.errorMap.set('username', '');
    }

    if (this.userForm.controls['password'].errors?.['required']) {
      this.errorMap.set('password', 'Bitte tragen Sie ein neues Passwort ein');
    } else {
      this.errorMap.set('password', '');
    }

    if (this.userForm.valid) {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }

      //create accountDTO
      const accountDTO: AccountModel = {
        name: (this.userForm.controls['name'].value || ''),
        username: (this.userForm.controls['username'].value || ''),
        password: (this.userForm.controls['password'].value || ''),
        role: (this.userForm.controls['role'].value || '')
      }
      
      // put accountDTO
      this.accountService.putAccount(accountDTO).subscribe({
        next: (data) => {
          this.dialogRef.close()
          this.openSnackBar(this.accountDTO.username!)
        },
        // if backend validation produces exceptions on postPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item: any) => {
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
