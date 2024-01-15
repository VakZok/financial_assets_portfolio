import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PurchaseModel} from "../../../../../../../core/models/purchase.model";
import {format} from "date-fns";
import {PurchaseService} from "../../../../../../../core/services/purchase.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PortfolioItemService} from "../../../../../../../core/services/portfolio-item.service";
import {PortfolioItemModel} from "../../../../../../../core/models/portfolio-item.model";

@Component({
  selector: 'app-purchase-dialog',
  templateUrl: './purchase-dialog.component.html',
  styleUrls: ['./purchase-dialog.component.css']
})

export class PurchaseDialogComponent implements OnInit {
  public purchaseForm: FormGroup<{quantity: FormControl<string | null>}>;
  loading: boolean = false;
  sending: boolean = false;
  isin: string = '';
  metaData: PortfolioItemModel = {};
  errorMap = new Map<string, string>([
    ['quantity', ''],
  ]);

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<PurchaseDialogComponent>,
    public pItemService: PortfolioItemService,
    public purchaseService: PurchaseService,
    private snackBar: MatSnackBar){

    this.isin = data.shareDTO.isin;
    this.purchaseForm = new FormGroup({
      quantity: new FormControl('', {
        validators:[
          Validators.required,
          Validators.min(1)],
        updateOn: 'submit'}),
    })
  }

  ngOnInit(){
    this.getData(this.isin);
  }

  getData(isin:string) {
    this.loading = true
    this.pItemService.getPItemSwagger(isin).subscribe({
      next: (data) => {
        this.metaData = data;
        this.loading = false;
      }
    })
  }

  // function to prevent other characters than digits
  onInputQuantity(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9]/g, '');
  }

  onSubmit() {
    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.purchaseForm.controls) {
      if (this.purchaseForm.controls.hasOwnProperty(controlName)) {
        const control = this.purchaseForm.get(controlName);
        if (control?.value != null) {
          control?.setValue(control?.value.trim())
        }
      }
    }

    // map  validation errors
    if (this.purchaseForm.controls['quantity'].errors?.['required']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else if (this.purchaseForm.controls['quantity'].errors?.['min']) {
      this.errorMap.set('quantity', 'Bitte tragen Sie eine Anzahl ein');
    } else {
      this.errorMap.set('quantity', '');
    }

    if (this.purchaseForm.valid) {
      // initialize errors if form is valid
      for (let [key] of this.errorMap) {
        this.errorMap.set(key, '');
      }
      //create purchaseDTO
      const purchaseDTO: PurchaseModel = {
        purchaseDate: format(new Date(), 'yyyy-MM-dd'),
        purchasePrice: this.metaData.currentPurchasePrice,
        quantity: parseInt(this.purchaseForm.controls['quantity'].value || ''),
      }
      this.sending = true
      // post purchaseDTO
      this.purchaseService.postPurchase(this.isin, purchaseDTO).subscribe({
        next: () => {
          this.sending = false
          this.dialogRef.close()
          this.openSnackBar(this.isin)
        },
        // if backend validation produces exceptions on postPItem, set them on the errorMap
        error: (errors) => errors.error.forEach((item: any) => {
          this.sending = false;
          this.purchaseForm.get(item.name)?.setErrors(item.message);
          this.errorMap.set(item.name, item.message);
        }),
      })
    }
  }

  // snackbar for success
  openSnackBar(isin:string) {
    this.snackBar.open('Order für "' + isin + '" wurde erfolgreich ausgeführt ✔️', '', {
      duration: 3000
    });
  }

}
