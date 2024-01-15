import {Component, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {PurchaseService} from "../../../../../../core/services/purchase.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {IsinValidator} from "../../../../../../core/validators/isin-validator";
import {first} from "rxjs";
import {ShareModel} from "../../../../../../core/models/share.model";
import {PurchaseModel} from "../../../../../../core/models/purchase.model";
import {format} from "date-fns";
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";


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
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent {
  pItemForm: FormGroup;
  metaData: PortfolioItemModel | undefined;
  sending: boolean = false
  errorMap = new Map<string, string>([
    ['isin', ''],
    ['quantity', ''],
  ]);

  @ViewChild(FormGroupDirective) form: any;

  constructor(private pItemService: PortfolioItemService,
              private purchaseService: PurchaseService,
              private snackBar: MatSnackBar,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.pItemForm = new FormGroup({
      isin: new FormControl('', {
        asyncValidators:[IsinValidator(this.pItemService)],
        validators:[
          Validators.required,
          Validators.maxLength(12)],
        updateOn:'blur'}),
      quantity: new FormControl('', [
        Validators.required,
        Validators.min(1)]),
    })
  }

  // get data for preview
  getData(isin:string) {
    if (isin !== '') {
      this.pItemService.getPItemSwagger(isin).subscribe({
        next: (data) => {this.metaData = data},
        error:()=> {
          this.pItemForm.controls['isin'].setErrors([{'isinUnknown': true}]);
          this.errorMap.set('isin', 'ISIN nicht bekannt.');
        }
      })
    }
  }

  // send get request after blurring isin input field
  async onBlurISIN(event:Event) {
    const inputElement = event.target as HTMLInputElement;
    this.getData(inputElement.value);
    this.pItemForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(() => {
      if (this.pItemForm.controls['isin'].errors?.['pItemExists']){
        this.errorMap.set('isin', 'ISIN bereits vorhanden');
      }
    })
  }

  // function to prevent other characters than digits
  onInputQuantity(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9]/g, '');
  }

  async onSubmit() {

    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.pItemForm.controls) {
      if (this.pItemForm.controls.hasOwnProperty(controlName)) {
        const control = this.pItemForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim());
        }
      }
    }

    this.getData(this.pItemForm.controls['isin'].value)


    // map errors that are not async
    if (this.pItemForm.controls['isin'].errors?.['maxlength']) {
      this.errorMap.set('isin', 'Die ISIN darf maximal aus 12 Stellen bestehen');
    } else if (this.pItemForm.controls['isin'].errors?.['required']) {
      this.errorMap.set('isin', 'Bitte füllen sie die ISIN aus');
    } else {
      this.errorMap.set('isin', '');
    }

    if (this.pItemForm.controls['quantity'].errors?.['required']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else if (this.pItemForm.controls['quantity'].errors?.['min']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else {
      this.errorMap.set('quantity', '');
    }

    // wait until Form Validation has finished
    await waitForFormNotPending(this.pItemForm)
    // map async isin error
    if (this.pItemForm.invalid) {
      if (this.pItemForm.controls['isin'].errors?.['pItemExists']) {
        this.errorMap.set('isin', 'ISIN bereits vorhanden');
      }
    } else if (this.pItemForm.valid) {
      console.log("check")
      if (this.pItemForm.valid) {
        // initialize errors if form is valid
        for (let [key] of this.errorMap) {
          this.errorMap.set(key, '');
        }

        // create shareDTO from pItemForm
        const shareDTO: ShareModel = {
          isin: this.pItemForm.controls['isin'].value || '',
        }

        //create purchaseDTO from pItemForm
        const purchaseDTO: PurchaseModel = {
          purchaseDate: format(new Date(), 'yyyy-MM-dd'),
          purchasePrice: this.metaData?.currentPurchasePrice,
          quantity: parseInt(this.pItemForm.controls['quantity'].value || ''),
          shareDTO: shareDTO
        }
        this.sending = true;
        // on success reset form
        this.purchaseService.postNewPItem(purchaseDTO).subscribe({
          next: () => {
            this.sending = false;
            this.pItemForm.reset();
            this.form.resetForm();
          },
          // if backend-validation produces exceptions on postNewPItem, set them on the errorMap
          error: (errors) => errors.error.forEach((item: any) => {
            this.sending = false;
            this.pItemForm.get(item.name)?.setErrors(item.message);
            this.errorMap.set(item.name, item.message);
          }),
          complete: () => this.onSuccess(shareDTO.isin!)
        })
      }
    }
  }

  // method to deeply clear the input form
  clearForm() {
    this.form.resetForm();
    this.pItemForm.reset();
    this.metaData = {};
    for (let [key] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

  // method to navigate back, and open snackbar for success
  onSuccess(isin:string){
      this.snackBar.open('Neues Portfolio-Item "' + isin + '" erfolgreich hinzugefügt ✔️', '', {
        duration: 3000
      });
    this.router.navigate(['meinPortfolio'])
  }

}
