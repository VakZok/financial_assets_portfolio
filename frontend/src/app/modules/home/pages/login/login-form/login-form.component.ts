import {Component, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {AuthenticationService} from "../../../../../core/services/authentication.service";
import {Router} from "@angular/router";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {first} from "rxjs";
import {LoginModel} from "../../../../../core/models/login.model";
import {AuthCoreService} from "../../../../../core/authentication/auth-core.service";

async function waitForFormNotPending(formGroup: FormGroup): Promise<void> {
  return new Promise<void>((resolve) => {
    formGroup.updateValueAndValidity()
    const subscription = formGroup.statusChanges.subscribe((status) => {
      if (status !== 'PENDING') {
        subscription.unsubscribe(); // Unsubscribe when not pending
        resolve();
      }
    });
  });
}

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  LoginForm: FormGroup;

  errorMap = new Map<string, string>([
    ['username', ''],
    ['password', ''],
    ['backendError', '']
  ]);

  @ViewChild(FormGroupDirective) form: any;

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private authenticationService: AuthenticationService,
              private authCoreService: AuthCoreService,
              private formBuilder: FormBuilder,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.LoginForm = new FormGroup({
      username: new FormControl('', {
        //asyncValidators: [UsernameValidator(this.authenticationService)],
        validators: [
          Validators.required
        ]
      }),
      password: new FormControl('', [
        Validators.required
      ])
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

  async onSubmit() {
    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.LoginForm.controls) {
      if (this.LoginForm.controls.hasOwnProperty(controlName)) {
        const control = this.LoginForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }

    // map errors that are not async
    if (this.LoginForm.controls['username'].errors?.['required']) {
      this.errorMap.set('username', 'Bitte geben Sie Ihren Benutzernamen ein');
    } else {
      this.errorMap.set('username', '');
    }

    if (this.LoginForm.controls['password'].errors?.['required']) {
      this.errorMap.set('password', 'Bitte geben Sie Ihr Passwort ein');
    } else {
      this.errorMap.set('password', '');
    }



    if (this.LoginForm.valid) {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }



      this.authCoreService.login(
        this.LoginForm.controls['username'].value || '',
          this.LoginForm.controls['password'].value || '').subscribe(
        (response: any) => {
          // Handle successful login response here
          console.log('Login successful', response);
          this.router.navigate(['meinPortfolio']);
          //console.log(this.authCoreService.getRole());
        },
        (error) => {
          this.errorMap.set('backendError', 'Eingegebener Benutzername oder Passwort nicht korrekt.')
          this.router.navigate(['login']);
        }
      );
      console.log("Finished")

    }
  }
}
