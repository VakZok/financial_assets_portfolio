import {Component, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {PurchaseService} from "../../../../../../core/services/purchase.service";
import {CurrencyPipe} from "@angular/common";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {WKNValidator} from "../../../../../../core/validators/wkn-validator";
import {first} from "rxjs";
import {ShareModel} from "../../../../../../core/models/share.model";
import {PurchaseModel} from "../../../../../../core/models/purchase.model";
import {format} from "date-fns";


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

const maxSigns : number = 255;


@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent {
  pItemForm: FormGroup;

  errorMap = new Map<string, string>([
    ['wkn', ''],
    ['name', ''],
    ['description', ''],
    ['category', ''],
    ['quantity', ''],
    ['purchaseDate', ''],
    ['purchasePrice', '']
  ]);

  leftSigns: string = maxSigns.toString();
  @ViewChild(FormGroupDirective) form: any;

  constructor(private pItemService: PortfolioItemService,
              private purchaseService: PurchaseService,
              private currencyPipe: CurrencyPipe,
              private snackBar: MatSnackBar,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.pItemForm = new FormGroup({
      wkn: new FormControl('', {
        asyncValidators:[WKNValidator(this.pItemService)],
        validators:[
          Validators.required,
          Validators.maxLength(6)],
        updateOn:'blur'}),
      name: new FormControl('',
        Validators.required),
      description: new FormControl('', [
        Validators.required,
        Validators.maxLength(255)]),
      category: new FormControl('',
        Validators.required),
      quantity: new FormControl('', [
        Validators.required,
        Validators.min(1)]),
      purchasePrice: new FormControl('', [
        Validators.required,
        Validators.min(1e-7)
      ])
    })
  }

  // send get request after blurring wkn input field
  async onBlurWKN() {
    this.pItemForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(() => {
      if (this.pItemForm.controls['wkn'].errors?.['pItemExists']){
        this.errorMap.set('wkn', 'Portfolio-Item mit dieser WKN bereits vorhanden');
      }
    })
  }

  // function that counts the amount of left signs
  onKeyUpDescription(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.leftSigns = String(maxSigns - inputElement.value.length)
  }
  // function to prevent other characters than digits
  onInputQuantity(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9]/g, '');
  }
  //function to prevent writing more than one comma into purchasePrice
  onKeyDownPrice(event: KeyboardEvent) {
    const inputElement = event.target as HTMLInputElement;
    if (event.key == ',' && inputElement.value.length == 0){
      event.preventDefault();
    }
  }
  //function that formats purchase Price
  onInputPurchasePrice(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(
      /[^\d,]/g,'').replace(
      /(,.*)\,/g,'$1').replace(/^(\d+\,?\d*).*/g,'$1');

  }

  transformPrice(event:Event){
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = this.currencyPipe.transform(
      inputElement.value.replace(
        ',', '.'), 'EUR', 'symbol', '.2-5') || '';
  }

  async onSubmit() {


    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.pItemForm.controls) {
      if (this.pItemForm.controls.hasOwnProperty(controlName)) {
        const control = this.pItemForm.get(controlName);
        if (control?.value != null && controlName !== 'purchasePrice') {
          control?.setValue(control?.value.trim())
        }
      }
    }


    // map errors that are not async
    if (this.pItemForm.controls['wkn'].errors?.['maxlength']) {
      this.errorMap.set('wkn', 'Die WKN darf maximal aus 6 Stellen bestehen');
    } else if (this.pItemForm.controls['wkn'].errors?.['required']) {
      this.errorMap.set('wkn', 'Bitte füllen sie die WKN aus');
    } else {
      this.errorMap.set('wkn', '');
    }

    if (this.pItemForm.controls['name'].errors?.['required']) {
      this.errorMap.set('name', 'Bitte tragen Sie einen Namen ein');
    } else {
      this.errorMap.set('name', '');
    }

    if (this.pItemForm.controls['description'].errors?.['maxLength']) {
      this.errorMap.set('description', 'Die Beschreibung darf nicht länger als 255 Zeichen sein');
    } else if (this.pItemForm.controls['description'].errors?.['required']) {
      this.errorMap.set('description', 'Bitte tragen Sie die Beschreibung ein');
    } else {
      this.errorMap.set('description', '');
    }

    if (this.pItemForm.controls['category'].errors?.['required']) {
      this.errorMap.set('category', 'Bitte wählen Sie eine Kategorie');
    } else {
      this.errorMap.set('category', '');
    }

    if (this.pItemForm.controls['quantity'].errors?.['required']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else if (this.pItemForm.controls['quantity'].errors?.['min']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else {
      this.errorMap.set('quantity', '');
    }

    if (this.pItemForm.controls['purchasePrice'].errors?.['required']) {
      this.errorMap.set('purchasePrice', 'Bitte tragen Sie einen Kaufpreis ein');
    } else if (this.pItemForm.controls['purchasePrice'].errors?.['min']) {
      this.errorMap.set('purchasePrice', 'Der Kaufpreis muss größer als 0 sein');
    } else {
      this.errorMap.set('purchasePrice', '');
    }

    // wait until Form Validation has finished
    await waitForFormNotPending(this.pItemForm)

    // map async wkn error
    if ( this.pItemForm.invalid) {
      if (this.pItemForm.controls['wkn'].errors?.['pItemExists']) {
        this.errorMap.set('wkn', 'Portfolio-Item mit dieser WKN bereits vorhanden');
      }
    }

    if (this.pItemForm.valid) {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }

      // create shareDTO from pItemForm
      const shareDTO: ShareModel = {
        wkn: this.pItemForm.controls['wkn'].value || '',
        name: this.pItemForm.controls['name'].value || '',
        category: this.pItemForm.controls['category'].value || '',
        description: this.pItemForm.controls['description'].value || '',
      }

      //create purchaseDTO from pItemForm
      const purchaseDTO: PurchaseModel = {
        purchaseDate: format(new Date(), 'yyyy-MM-dd'),
        purchasePrice: parseFloat(this.pItemForm.controls['purchasePrice'].value?.replace(',', '.') || ''),
        quantity: parseInt(this.pItemForm.controls['quantity'].value || ''),
        shareDTO: shareDTO
      }

      // on success reset form
      this.purchaseService.postNewPItem(purchaseDTO).subscribe({
        next: () => {
          this.pItemForm.reset();
          this.form.resetForm();
          this.leftSigns = maxSigns.toString();
        },
        // if backend-validation produces exceptions on postNewPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item: any) => {
          this.pItemForm.get(item.name)?.setErrors(item.message)
          this.errorMap.set(item.name, item.message);
        }),
        complete: () => this.onSuccess(shareDTO.wkn!)
      })
    }
  }

  // method to deeply clear the input form
  clearForm() {
    this.form.resetForm();
    this.pItemForm.reset();
    this.leftSigns = '255';
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

  // method to navigate back, and open snackbar for success
  onSuccess(wkn:string){
      this.snackBar.open('Neues Portfolio-Item "' + wkn + '" erfolgreich hinzugefügt ✔️', '', {
        duration: 3000
      });
    this.router.navigate(['meinPortfolio'])

  }

}