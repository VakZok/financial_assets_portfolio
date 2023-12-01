import {Component, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {AccountModel} from "../../../../../core/models/account.model";
import {AuthenticationService} from "../../../../../core/services/authentication.service";
import {Router} from "@angular/router";
import {first} from "rxjs";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {passwordMatchValidator} from "../../../../../core/validators/passwordMatch-validator";

@Component({
  selector: 'app-account-form',
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.css']
})
export class AccountFormComponent {
  AccountForm: FormGroup = new FormGroup({});

  errorMap = new Map<string, string>([
    ['username', ''],
    ['name', ''],
    ['password', ''],
    ['validatePassword', '']
  ]);

  @ViewChild(FormGroupDirective) form: any;

  /*
    passwordMatchValidator(passwordKey: string, passwordConfirmationKey: string) {
        return (group: FormGroup) => {
            let passwordInput = group.controls[passwordKey],
                passwordConfirmationInput = group.controls[passwordConfirmationKey];
            if (passwordInput.value !== passwordConfirmationInput.value) {
                return passwordConfirmationInput.setErrors({notEquivalent: true})
            }
            else {
                return passwordConfirmationInput.setErrors(null);
            }
        }
    }
   */



  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.AccountForm = new FormGroup({
        username: new FormControl('', {
            asyncValidators:[UsernameValidator(this.authenticationService)],
            validators:[
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(30)],
            updateOn:'blur'}),
        name: new FormControl('', [
            Validators.required,
            Validators.maxLength(30)]),
        password: new FormControl('', [
            Validators.required,
            Validators.minLength(12),
            Validators.maxLength(30)]),
        confirmPassword: new FormControl('', [
            Validators.required,
            Validators.maxLength(30),
        ])
    }, {validators: passwordMatchValidator(this.AccountForm)})
  }

  // send get request after blurring password input field
  async onBlurUsername() {
    this.AccountForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.AccountForm.controls['username'].errors?.['UsernameExists']){
        this.errorMap.set('username', 'Dieser benutzername ist bereits vergeben.');
      }
    })
  }

  // method to deeply clear the input form
  clearForm() {
    this.form.resetForm();
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
