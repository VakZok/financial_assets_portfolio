import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AccountModel} from "../../../../../core/models/account.model";
import {AuthenticationService} from "../../../../../core/services/authentication.service";
import {Router} from "@angular/router";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {first} from "rxjs";

@Component({
  selector: 'app-account-form',
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.css']
})
export class AccountFormComponent {
  AccountForm: FormGroup;

  errorMap = new Map<string, string>([
    ['username', ''],
    ['name', ''],
    ['password', ''],
    ['name', '']
  ]);

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private authenticationService: AuthenticationService,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.AccountForm = new FormGroup({
      username: new FormControl('', {
        asyncValidators: [UsernameValidator(this.authenticationService)],
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ],
        updateOn: 'blur'
      }),
      name: new FormControl('', {
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ]
      }),
      password: new FormControl('', {
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ]
      }),
      validatePassword: new FormControl('', {
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ]
      })
    });
  }

  // send get request after blurring password input field
  async onBlurUsername() {
    this.AccountForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.AccountForm.controls['username'].errors?.['UsernameExists']){
        this.errorMap.set('username', 'Dieser benutzername ist bereits vergeben');
      }
    })
  }

  // method to deeply clear the input form
  clearForm() {
    this.AccountForm.reset();
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

  onSubmit() {
    if (this.AccountForm.valid) {
      console.log('Willkommen!', this.AccountForm.value);
    } else {
      // Form is invalid, display error messages or take appropriate action
      console.log('Form is invalid. Please check the fields.');
    }
  }
}
