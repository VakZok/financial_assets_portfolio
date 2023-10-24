import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DateValidator} from "../../../../../../core/validators/date-validator";
import {ShareModel} from "../../../../../../core/models/share.model"
import {ShareService} from "../../../../../../core/services/share.service";
import {PortfolioItemService} from "../../../../../../core/services/portfolio-item.service";
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";
import {ExceptionsModel} from "../../../../../../core/models/exceptions.model";

const maxDate = new Date("2123-12-31");
const minDate: Date = new Date("1903-04-22");
const maxSigns : number = 255;

@Component({
  selector: 'item-input-form',
  templateUrl: './item-input-form.component.html',
  styleUrls: ['./item-input-form.component.css']
})

export class ItemInputFormComponent implements OnInit {

  shares: ShareModel[] = [];
  sharesFiltered: ShareModel[] = [];

  errorMap = new Map<string, string>([
    ['wkn', ''],
    ['name', ''],
    ['description', ''],
    ['cat', ''],
    ['quantity', ''],
    ['purchaseDate', ''],
    ['purchasePrice', '']
  ]);

  leftSigns: string = '255';
  itemAdded: boolean = false;

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
    cat: new FormControl('',
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

  constructor(private shareService: ShareService, private pItemService: PortfolioItemService) {
  }

  ngOnInit() {
    this.shareService.getShares().subscribe({
      next: (data) => this.shares = data,
      error: (error) => console.error(error),
      complete: () => console.info('complete')
    })
  }

  // function that counts the amount of left signs
  onKeyUpDescription(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.leftSigns = String(maxSigns - inputElement.value.length)
  }

  //function to prevent writing more than one comma into purchasePrice
  onKeyDownPrice(event: KeyboardEvent) {
    const inputElement = event.target as HTMLInputElement;
    if (event.key == ',' && inputElement.value.length == 0){
      event.preventDefault();
    }
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

      if(this.pItemForm.controls.cat.errors?.['required']){
        this.errorMap.set('cat', 'Bitte wählen Sie eine Kategorie');
      } else {
        this.errorMap.set('cat', '');
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

      // create shareDTO
      let shareDTO: ShareModel = {
        wkn: this.pItemForm.controls.wkn.value || '',
        name: this.pItemForm.controls.name.value || '',
        category: this.pItemForm.controls.cat.value || '',
        description: this.pItemForm.controls.description.value || ''
      }
      //create portfolioItemDTO
      const pItemDTO: PortfolioItemModel = {
        purchaseDate: new Date(this.pItemForm.controls.purchaseDate.value || ''),
        purchasePrice: parseFloat(this.pItemForm.controls.purchasePrice.value?.replace(',', '.') || ''),
        quantity: parseInt(this.pItemForm.controls.quantity.value || ''),
        shareDTO: shareDTO
      }

      //check if share exists, if exists, sends a put request, if not, sends a post request to add/modify share
      //Finally portfolioItemDTO is sent to backend using post request
      this.shareService.getShare(shareDTO.wkn).subscribe({
        next: (data) => { //if share does not exist in database, an empty shareDTO is returned
          if (data.wkn != null && data.name != null && data.category != null && data.description != null) {
            // if returned shareDTO is not empty, send a put request to modify attributes of existing share in database
            this.shareService.putShare(shareDTO.wkn, shareDTO).subscribe({
              next:(data: ShareModel) => {
                //after successful put request, add portfolioItem with a post request
                this.pItemService.postPItem(pItemDTO).subscribe({
                  next: (data) => {
                    console.log(data)
                    this.pItemForm.reset();
                  },
                  // if backend validation produces exceptions on postPItem, set them on the errorMap
                  error: (errors) => errors.error.forEach((item:any) => {
                    this.errorMap.set(item.name, item.message);
                  }),
                  complete: () => this.itemAdded = true
                })
              },
              // if backend validation produces exceptions on putShare, set them on the errorMap
              error: (errors) => errors.error.forEach((item:any) => {
                this.errorMap.set(item.name, item.message);
              }),
            })
          } else { // if an empty share is returned, add the share to database using post request
            this.shareService.postShare(shareDTO).subscribe({
              next:() => {
                //afterwards add portfolioItem using post request
                this.pItemService.postPItem(pItemDTO).subscribe({
                  next: (data) => {
                    console.log(data)
                    this.pItemForm.reset();
                  },
                  // if backend validation produces exceptions on postPItem, set them on the errorMap
                  error: (errors) => errors.error.forEach((item:any) => {
                    this.errorMap.set(item.name, item.message);
                  }),
                  complete: () => this.itemAdded = true
                })
              },
              // if backend validation produces exceptions on postShare, set them on the errorMap
              error: (errors) => errors.error.forEach((item:any) => {
                this.errorMap.set(item.name, item.message);
              }),
            })
          }
        },
      })
    }
  }

  // method to clear the input form
  clearForm() {
    this.pItemForm.reset();
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

}
