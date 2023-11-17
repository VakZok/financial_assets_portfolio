import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ShareModel} from "../../../../../../../core/models/share.model";
import {CurrencyPipe} from "@angular/common";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {WKNValidator} from "../../../../../../../core/validators/wkn-validator";
import {PurchaseModel} from "../../../../../../../core/models/purchase.model";
import {format} from "date-fns";
import {PurchaseService} from "../../../../../../../core/services/purchase.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-purchase-dialog',
  templateUrl: './purchase-dialog.component.html',
  styleUrls: ['./purchase-dialog.component.css']
})
export class PurchaseDialogComponent {
  public purchaseForm: FormGroup<{ quantity: FormControl<string | null>; purchasePrice: FormControl<string | null> }>;

  errorMap = new Map<string, string>([
    ['wkn', ''],
    ['name', ''],
    ['description', ''],
    ['category', ''],
    ['quantity', ''],
    ['purchaseDate', ''],
    ['purchasePrice', '']
  ]);
  shareDTO: ShareModel = {}

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, public dialogRef: MatDialogRef<PurchaseDialogComponent>,
    private currencyPipe: CurrencyPipe, public purchaseService: PurchaseService, private router: Router,
    private snackBar: MatSnackBar){

    this.shareDTO = data.shareDTO;
    this.purchaseForm = new FormGroup({
      quantity: new FormControl('', [
        Validators.required,
        Validators.min(1)]),
      purchasePrice: new FormControl('', [
        Validators.required,
        Validators.min(1e-7)
      ])
    })
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

  onSubmit() {

    //replace ',' with '.' of purchasePrice for further processing
    const price = this.purchaseForm.get('purchasePrice')?.value;
    if (price !== null && price !== undefined) {
      this.purchaseForm.get('purchasePrice')?.setValue(price.replace(',', '.'));
    }

    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.purchaseForm.controls) {
      if (this.purchaseForm.controls.hasOwnProperty(controlName)) {
        const control = this.purchaseForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }

    if (this.purchaseForm.controls['quantity'].errors?.['required']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else if (this.purchaseForm.controls['quantity'].errors?.['min']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else {
      this.errorMap.set('quantity', '');
    }

    if (this.purchaseForm.controls['purchasePrice'].errors?.['required']) {
      this.errorMap.set('purchasePrice', 'Bitte tragen Sie einen Kaufpreis ein');
    } else if (this.purchaseForm.controls['purchasePrice'].errors?.['min']) {
      this.errorMap.set('purchasePrice', 'Der Kaufpreis muss größer als 0 sein');
    } else {
      this.errorMap.set('purchasePrice', '');
    }

    if (price !== null && price !== undefined) {
      this.purchaseForm.get('purchasePrice')?.setValue(price.replace('.', ','));
    }

    if (this.purchaseForm.valid) {

      // initialize errors if form is valid
      for (let [key, error] of this.errorMap) {
        this.errorMap.set(key, '');
      }

      //create portfolioItemDTO
      const purchaseDTO: PurchaseModel = {
        purchaseDate: format(new Date(), 'yyyy-MM-dd'),
        purchasePrice: parseFloat(this.purchaseForm.controls['purchasePrice'].value?.replace(',', '.') || ''),
        quantity: parseInt(this.purchaseForm.controls['quantity'].value || ''),
      }

      this.purchaseService.postPurchase(this.shareDTO.wkn || '', purchaseDTO).subscribe({
        next: (data) => {
          this.dialogRef.close()
          this.openSnackBar(this.shareDTO.wkn!)
        },
        // if backend validation produces exceptions on postPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item: any) => {
        }),
      })
    }
  }

  openSnackBar(wkn:string) {
    this.snackBar.open('Order für "' + wkn + '" wurde erfolgreich ausgeführt ✔️', '', {
      duration: 3000
    });
  }



}
