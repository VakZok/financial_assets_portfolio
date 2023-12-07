import {Component, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {AccountModel} from "../../../../../core/models/account.model";
import {first} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UsernameValidator} from "../../../../../core/validators/username-validator";
import {UserManagementService} from "../../../../../core/services/user-management.service";
import {Router} from "@angular/router";

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
  accountForm: FormGroup = new FormGroup({});

  errorMap = new Map<string, string>([
    ['username', ''],
    ['name', ''],
    ['password', ''],
    ['confirmPassword', ''],
    ['role', '']
  ]);

  @ViewChild(FormGroupDirective) form: any;

  // Form Group Validator to ensure that password and username are not longer than 30 characters
  constructor(
      private router: Router,
      private userManService: UserManagementService,
      private snackBar: MatSnackBar) {

    /* Form Validation, check for completeness and sanity */
      this.accountForm = new FormGroup({
          username: new FormControl('', {
              asyncValidators: [UsernameValidator(this.userManService)],
              validators: [
                Validators.required,
                Validators.maxLength(30)],
              updateOn: 'blur'
          }),
          name: new FormControl('', [
              Validators.required,
              Validators.maxLength(30)
          ]),
          password: new FormControl('', [
              Validators.required,
              Validators.maxLength(30)
          ]),
          confirmPassword: new FormControl('', [
              Validators.required,
          ]),
          role: new FormControl('', [
              Validators.required,
          ])
      });
  }

  // send get request after blurring password input field
  async onBlurUsername() {
    this.accountForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.accountForm.controls['username'].errors?.['usernameExists']){
        this.errorMap.set('username', 'Benutzername bereits vergeben.');
      }
    })
  }

    async onSubmit() {
        // loop over input form and remove leading/trailing whitespaces
        for (const controlName in this.accountForm.controls) {
            if (this.accountForm.controls.hasOwnProperty(controlName)) {
                const control = this.accountForm.get(controlName);
                if (control?.value != null) {
                    control?.setValue(control?.value.trim())
                }
            }
        }

        // map errors that are not async
        if (this.accountForm.controls['username'].errors?.['maxlength']) {
            this.errorMap.set('username', 'Der Benutzername darf maximal 30 Zeichen lang sein');
        } else if (this.accountForm.controls['username'].errors?.['required']) {
            this.errorMap.set('username', 'Bitte tragen Sie einen Benutzernamen ein');
        } else {
            this.errorMap.set('username', '');
        }

        if (this.accountForm.controls['name'].errors?.['required']) {
            this.errorMap.set('name', 'Bitte tragen Sie Ihren Namen ein');
        } else if (this.accountForm.controls['name'].errors?.['maxlength']) {
            this.errorMap.set('username', 'Der Benutzername darf maximal 30 Zeichen lang sein');
        } else {
            this.errorMap.set('name', '');
        }

        if (this.accountForm.controls['password'].errors?.['maxLength']) {
            this.errorMap.set('password', 'Das Passwort darf nicht länger als 30 Zeichen sein');
        } else if (this.accountForm.controls['password'].errors?.['required']) {
            this.errorMap.set('password', 'Bitte vergeben Sie ein Passwort');
        } else {
            this.errorMap.set('password', '');
        }

        if (this.accountForm.controls['confirmPassword'].errors?.['required']) {
            this.errorMap.set('confirmPassword', 'Bitte bestätigen sie das Passwort');
        } else {
            this.errorMap.set('confirmPassword', '');
        }

        if (this.accountForm.controls['password']?.value !== this.accountForm.controls['confirmPassword']?.value) {
            this.accountForm.controls['password'].setErrors(['passwordMismatch'])
            this.accountForm.controls['confirmPassword'].setErrors(['passwordMismatch'])
            this.errorMap.set('password', 'Passwörter stimmen nicht überein');
            this.errorMap.set('confirmPassword', 'Passwörter stimmen nicht überein');
        }


        if (this.accountForm.controls['role'].errors?.['required']) {
            this.errorMap.set('role', 'Bitte wählen sie eine Rolle');
        } else {
            this.errorMap.set('role', '');
        }

        // wait until Form Validation has finished
        await waitForFormNotPending(this.accountForm)

        // map async username error
        if (this.accountForm.invalid) {
            if (this.accountForm.controls['username'].errors?.['usernameExists']) {
                this.errorMap.set('username', 'Benutzername ist bereits vergeben');
            }
        }

        if (this.accountForm.valid) {
            // initialize errors if form is valid
            for (let [key, error] of this.errorMap) {
                this.errorMap.set(key, '');
            }

            // create shareDTO from AccountForm
            const accountDTO: AccountModel = {
                username: this.accountForm.controls['username'].value || '',
                name: this.accountForm.controls['name'].value || '',
                password: this.accountForm.controls['password'].value || '',
                role: this.accountForm.controls['role'].value || '',
            }

            // on success reset form
            this.userManService.postAccount(accountDTO).subscribe({
                next: () => {
                    this.accountForm.reset();
                    this.form.resetForm();
                    this.router.navigate(["/benutzer"])
                },
                // if backend-validation produces exceptions on postNewPItem, set them on the errorMap
                error: (errors) => errors.error.forEach((item: any) => {
                    this.accountForm.get(item.name)?.setErrors(item.message)
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
        this.accountForm.reset();
        for (let [key, error] of this.errorMap){
            this.errorMap.set(key, '');
        }
    }
}
