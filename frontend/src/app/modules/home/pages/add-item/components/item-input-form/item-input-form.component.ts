import { Component, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";
import {ShareModel} from "../../../../../../core/models/share.model";
import {ShareService} from "../../../../../../core/services/share.service";
import {WKNValidator} from "../../../../../../core/validators/wkn-validator";
import {first} from "rxjs";
import {format} from 'date-fns';
import {CurrencyPipe} from "@angular/common";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SnackBarComponent} from "../snack-bar/snack-bar.component";
import {Router} from "@angular/router";

const maxDate = new Date("2123-12-31");
const minDate: Date = new Date("1903-04-22");
const maxSigns : number = 255;

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
  selector: 'item-input-form',
  templateUrl: './item-input-form.component.html',
  styleUrls: ['./item-input-form.component.css']
})

export class ItemInputFormComponent {

  pItemForm: FormGroup;
  formattedPrice: string | null = '';


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
  itemAdded: boolean = false;
  @ViewChild(FormGroupDirective) form: any;




  constructor(private pItemService: PortfolioItemService,
              private shareService: ShareService,
              private currencyPipe: CurrencyPipe,
              private _snackBar: MatSnackBar,
              private router: Router) {

    /* Form Validation, check for completeness and sanity */
    this.pItemForm = new FormGroup({
      wkn: new FormControl('', {
        asyncValidators:[WKNValidator(shareService)],
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

  async onBlurWKN() {
    this.pItemForm.statusChanges.pipe(
      first(status => status !== 'PENDING')).subscribe(status => {
      if (this.pItemForm.controls['wkn'].errors?.['shareExist']){
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
      inputElement.value.replace(',', '.'), 'EUR', 'symbol', '.2-5') || '';
  }



  async onSubmit() {

    //initialize
    this.itemAdded = false;

    //replace ',' with '.' of purchasePrice for further processing
    const price = this.pItemForm.get('purchasePrice')?.value;
    if (price !== null && price !== undefined) {
      this.pItemForm.get('purchasePrice')?.setValue(price.replace(',', '.'));
    }

    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.pItemForm.controls) {
      if (this.pItemForm.controls.hasOwnProperty(controlName)) {
        const control = this.pItemForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }

    const errors = this.pItemForm.get('wkn')?.errors; // Get all active errors
    if (errors) {
      Object.keys(errors).forEach(errorKey => {
        console.log(`Error key: ${errorKey}, Error value: ${errors[errorKey]}`);
      });
    }

    if (this.pItemForm.controls['wkn'].errors?.['maxlength']) {

      this.errorMap.set('wkn', 'Die WKN darf maximal aus 6 Stellen bestehen');
    } else if (this.pItemForm.controls['wkn'].errors?.['required']) {
      this.errorMap.set('wkn', 'Bitte füllen sie die WKN aus');
    } else {
      this.errorMap.set('wkn', '');
    }

    if (this.pItemForm.controls['name'].errors?.['required']) {
      console.log('try')
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

    if (price !== null && price !== undefined) {
      this.pItemForm.get('purchasePrice')?.setValue(price.replace('.', ','));
    }

    await waitForFormNotPending(this.pItemForm)
    if ( this.pItemForm.invalid) {
      if (this.pItemForm.controls['wkn'].errors?.['shareExist']) {
        this.errorMap.set('wkn', 'Portfolio-Item mit dieser WKN bereits vorhanden');
      }
    }

    if (this.pItemForm.valid) {


      console.log('test2')
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }

      const shareDTO: ShareModel = {
        wkn: this.pItemForm.controls['wkn'].value || '',
        name: this.pItemForm.controls['name'].value || '',
        category: this.pItemForm.controls['category'].value || '',
        description: this.pItemForm.controls['description'].value || ''
      }

      //create portfolioItemDTO
      const pItemDTO: PortfolioItemModel = {
        purchaseDate: format(new Date(), 'yyyy-MM-dd'),
        purchasePrice: parseFloat(this.pItemForm.controls['purchasePrice'].value?.replace(',', '.') || ''),
        quantity: parseInt(this.pItemForm.controls['quantity'].value || ''),
        shareDTO: shareDTO
      }

      this.pItemService.postPItem(pItemDTO).subscribe({
        next: (data) => {
          this.pItemForm.reset();
          this.form.resetForm();
          this.leftSigns = maxSigns.toString();
        },
        // if backend validation produces exceptions on postPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item: any) => {
          this.pItemForm.get(item.name)?.setErrors(item.message)
          this.errorMap.set(item.name, item.message);
        }),
        complete: () => this.onSuccess()
      })
    }

  }

  // method to clear the input form
  clearForm() {
    this.itemAdded = false;
    this.form.resetForm();
    this.pItemForm.reset();
    this.leftSigns = '255';
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

  onSuccess(){
    this._snackBar.openFromComponent(SnackBarComponent, {
      duration: 2000,
    });
    this.router.navigate(['meinPortfolio'])

  }

}
