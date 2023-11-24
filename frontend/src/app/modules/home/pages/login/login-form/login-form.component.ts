import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
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
    ['userName', ''],
    ['password', '']
  ]);

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private authenticationService: AuthenticationService,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.LoginForm = new FormGroup({
      userName: new FormControl('', {
        asyncValidators: [UsernameValidator(this.authenticationService)],
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ],
        updateOn: 'blur'
      }),
      password: new FormControl('', {
        validators: [
          Validators.required,
          Validators.maxLength(30)
        ]
      })
    });
  }

  // send get request after blurring username input field
  async onBlurUsername() {
    this.LoginForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.LoginForm.controls['userName'].errors?.['UsernameExists']){
        this.errorMap.set('userName', 'Dieser benutzername ist bereits vergeben');
      }
    })
  }

  // method to deeply clear the input form
  clearForm() {
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
