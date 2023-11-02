import {Component,  ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";

const maxDate = new Date("2123-12-31");
const minDate: Date = new Date("1903-04-22");
const maxSigns : number = 255;

@Component({
  selector: 'item-input-form',
  templateUrl: './item-input-form.component.html',
  styleUrls: ['./item-input-form.component.css']
})

export class ItemInputFormComponent {

  errorMap = new Map<string, string>([
    ['wkn', ''],
    ['name', ''],
    ['description', ''],
    ['category', ''],
    ['quantity', ''],
    ['purchaseDate', ''],
    ['purchasePrice', '']
  ]);

  leftSigns: string = '255';
  itemAdded: boolean = false;
  @ViewChild(FormGroupDirective) form: any;

  /* Form Validation, check for completeness and sanity */
  pItemForm = new FormGroup({
    wkn: new FormControl('', [
      Validators.required,
      Validators.minLength(6)]),
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
    purchaseDate: new FormControl('', [
      Validators.required,
      //DateValidator()
    ]),
    purchasePrice: new FormControl('', [
      Validators.required,
      Validators.min(1e-7)
      ])
  })

  constructor(private pItemService: PortfolioItemService) {
  }
  // do not allow more than 6 characters for wkn input field
  onInputWkn(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.slice(0,6)
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
  onSubmit() {

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
        if(control?.value != null){
          control?.setValue(control?.value.trim())
        }
      }
    }

    //check input form. If an input field is invalid, set error text
    if(this.pItemForm.invalid){
      if(this.pItemForm.controls.wkn.errors?.['minlength']){
        this.errorMap.set('wkn', 'Die WKN muss aus 6 Stellen bestehen');
      } else if (this.pItemForm.controls.wkn.errors?.['required']){
        this.errorMap.set('wkn','Bitte füllen sie die WKN aus');
      } else {
        this.errorMap.set('wkn', '');
      }

      if(this.pItemForm.controls.name.errors?.['required']){
        this.errorMap.set('name', 'Bitte tragen Sie einen Namen ein');
      } else {
        this.errorMap.set('name', '');
      }

      if(this.pItemForm.controls.description.errors?.['maxLength']){
        this.errorMap.set('description', 'Die Beschreibung darf nicht länger als 255 Zeichen sein');
      } else if (this.pItemForm.controls.description.errors?.['required']){
        this.errorMap.set('description', 'Bitte tragen Sie die Beschreibung ein');
      } else {
        this.errorMap.set('description', '');
      }

      if(this.pItemForm.controls.category.errors?.['required']){
        this.errorMap.set('category', 'Bitte wählen Sie eine Kategorie');
      } else {
        this.errorMap.set('category', '');
      }

      if(this.pItemForm.controls.quantity.errors?.['required']){
        this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
      } else if (this.pItemForm.controls.quantity.errors?.['min']){
        this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
      } else {
        this.errorMap.set('quantity', '');
      }

      if(this.pItemForm.controls.purchaseDate.errors?.['required']){
        this.errorMap.set('purchaseDate', 'Bitte tragen Sie ein Kaufdatum ein');
      } else if(this.pItemForm.controls.purchaseDate.errors?.['dateFutureErr']) {
        this.errorMap.set('purchaseDate',
          'Das Kaufdatum muss vor dem ' + maxDate.toLocaleDateString('de-DE',
            {day: 'numeric', month: 'numeric', year:'numeric'}) + ' liegen');

      } else if(this.pItemForm.controls.purchaseDate.errors?.['datePastErr']){
        this.errorMap.set('purchaseDate',
          'Das Kaufdatum muss nach dem ' + minDate.toLocaleDateString(
            'de-DE', {day: 'numeric', month: 'numeric', year:'numeric'}) + ' liegen');
      } else {
        this.errorMap.set('purchaseDate', '');
      }

      if (this.pItemForm.controls.purchasePrice.errors?.['required']){
        this.errorMap.set('purchasePrice', 'Bitte tragen Sie einen Kaufpreis ein');
      } else if (this.pItemForm.controls.purchasePrice.errors?.['min']){
        this.errorMap.set('purchasePrice', 'Der Kaufpreis muss größer als 0 sein');
      }else {
        this.errorMap.set('purchasePrice', '');
      }

      if (price !== null && price !== undefined) {
        this.pItemForm.get('purchasePrice')?.setValue(price.replace('.', ','));
      }

    } else {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap){
        this.errorMap.set(key, '');
      }

      //create portfolioItemDTO
      const pItemDTO: PortfolioItemModel = {
        purchaseDate: new Date(this.pItemForm.controls.purchaseDate.value || ''),
        purchasePrice: parseFloat(this.pItemForm.controls.purchasePrice.value?.replace(',', '.') || ''),
        quantity: parseInt(this.pItemForm.controls.quantity.value || ''),
        wkn: this.pItemForm.controls.wkn.value || '',
        name: this.pItemForm.controls.name.value || '',
        category: this.pItemForm.controls.category.value || '',
        description: this.pItemForm.controls.description.value || ''
      }
      //check if share exists, if exists, sends a put request, if not, sends a post request to add/modify share
      //Finally portfolioItemDTO is sent to backend using post request
      this.pItemService.postPItem(pItemDTO).subscribe({
        next: (data) => {
          console.log(data)
          this.pItemForm.reset();
          this.form.resetForm();
        },
        // if backend validation produces exceptions on postPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item:any) => {
          this.errorMap.set(item.name, item.message);
        }),
        complete: () => this.itemAdded = true
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

}
