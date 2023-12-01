import {Component, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {AccountModel} from "../../../../../core/models/account.model";
import {AuthenticationService} from "../../../../../core/services/authentication.service";
import {first} from "rxjs";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {passwordMatchValidator} from "../../../../../core/validators/passwordMatch-validator";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService,
              private snackBar: MatSnackBar) {

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
            Validators.required]),
        password: new FormControl('', [
            Validators.required,
            Validators.minLength(12),
            Validators.maxLength(30)]),
        confirmPassword: new FormControl('', [
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

    async onSubmit() {

        // loop over input form and remove leading/trailing whitespaces
        for (const controlName in this.AccountForm.controls) {
            if (this.AccountForm.controls.hasOwnProperty(controlName)) {
                const control = this.AccountForm.get(controlName);
                if (control?.value != null) {
                    control?.setValue(control?.value.trim())
                }
            }
        }

        // map errors that are not async
        if (this.AccountForm.controls['username'].errors?.['maxlength']) {
            this.errorMap.set('username', 'Der Benutzername darf maximal 30 Zeichen lang sein');
        } else if (this.AccountForm.controls['username'].errors?.['minLength']) {
            this.errorMap.set('username', 'Der Benutzername muss mindestens 6 Zeichen lang sein');
        } else if (this.AccountForm.controls['username'].errors?.['required']) {
            this.errorMap.set('username', 'Bitte tragen Sie einen Benutzernamen ein');
        } else {
            this.errorMap.set('username', '');
        }

        if (this.AccountForm.controls['name'].errors?.['required']) {
            this.errorMap.set('name', 'Bitte tragen Sie Ihren Namen ein');
        } else {
            this.errorMap.set('name', '');
        }

        if (this.AccountForm.controls['password'].errors?.['maxLength']) {
            this.errorMap.set('password', 'Das Passwort darf nicht länger als 30 Zeichen sein');
        } else if (this.AccountForm.controls['password'].errors?.['minLength']) {
            this.errorMap.set('password', 'Das Passwort muss mindestens 12 Zeichen haben');
        } else if (this.AccountForm.controls['password'].errors?.['required']) {
            this.errorMap.set('password', 'Bitte vergeben Sie ein Passwort');
        } else {
            this.errorMap.set('password', '');
        }

        if (this.AccountForm.errors?.['passwordMismatch']) {
            this.errorMap.set('confirmPassword', 'Stimmt nicht mit Passwort überein');
        } else {
            this.errorMap.set('confirmPassword', '');
        }

        // wait until Form Validation has finished
        await waitForFormNotPending(this.AccountForm)

        // map async wkn error
        if ( this.AccountForm.invalid) {
            if (this.AccountForm.controls['username'].errors?.['usernameExists']) {
                this.errorMap.set('username', 'Dieser Benutzername ist bereits vergeben. Bitte loggen Sie sich ein.');
            }
        }

        if (this.AccountForm.valid) {
            // initialize errors if form is valid
            for (let [key, error] of this.errorMap) {
                this.errorMap.set(key, '');
            }

            // create shareDTO from AccountForm
            const accountDTO: AccountModel = {
                username: this.AccountForm.controls['username'].value || '',
                name: this.AccountForm.controls['name'].value || '',
                password: this.AccountForm.controls['password'].value || '',
                role: 'Default' || '',
            }

            // on success reset form
            this.authenticationService.postAccount(accountDTO).subscribe({
                next: (data) => {
                    this.AccountForm.reset();
                    this.form.resetForm();
                },
                // if backend-validation produces exceptions on postNewPItem, set them on the errorMap
                error: (errors) => errors.error.forEach((item: any) => {
                    this.AccountForm.get(item.name)?.setErrors(item.message)
                    this.errorMap.set(item.name, item.message);
                }),
                complete: () => this.snackBar.open('Neuen Benutzer "' + accountDTO.username + '" erfolgreich hinzugefügt ✔️', '', {
                    duration: 3000
                })
            })
        }
    }

    // method to deeply clear the input form
    clearForm() {
        this.form.resetForm();
        this.AccountForm.reset();
        for (let [key, error] of this.errorMap){
            this.errorMap.set(key, '');
        }
    }
}
