import {Component, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {AccountModel} from "../../../../../core/models/account.model";
import {AuthenticationService} from "../../../../../core/services/authentication.service";
import {Router} from "@angular/router";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {first} from "rxjs";

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  LoginForm: FormGroup;

  errorMap = new Map<string, string>([
    ['username', ''],
    ['password', '']
  ]);

  @ViewChild(FormGroupDirective) form: any;

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private authenticationService: AuthenticationService,
              private formBuilder: FormBuilder,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.LoginForm = new FormGroup({
      username: new FormControl('', {
        asyncValidators: [UsernameValidator(this.authenticationService)],
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ],
        updateOn: 'blur'
      }),
      password: new FormControl('', [
        Validators.required,
        Validators.maxLength(30)]
      )
    })
  }

  // send get request after blurring username input field
  async onBlurUsername() {
    this.LoginForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.LoginForm.controls['username'].errors?.['UsernameExists']){
        this.errorMap.set('username', 'Dieser Benutzername ist bereits vergeben');
      }
    })
  }

  // method to deeply clear the input form
  clearForm() {
    this.form.resetForm();
    this.LoginForm.reset();
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

  onSubmit() {
    if (this.LoginForm.valid) {
      console.log('Willkommen!', this.LoginForm.value);
    } else {
      // Form is invalid, display error messages or take appropriate action
      console.log('Form is invalid. Please check the fields.');
    }
  }
}
