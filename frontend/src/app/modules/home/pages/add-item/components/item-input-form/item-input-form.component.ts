import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DateValidator} from "../../../../../../core/validators/date-validator";
import {ShareModel} from "../../../../../../core/models/share.model"
import {ShareService} from "../../../../../../core/services/share.service";

@Component({
  selector: 'item-input-form',
  templateUrl: './item-input-form.component.html',
  styleUrls: ['./item-input-form.component.css']
})

export class ItemInputFormComponent implements OnInit {

  shares: ShareModel[] = [];
  sharesFiltered: ShareModel[] = [];

  wknError: String = '';
  nameError: String = '';
  descriptionError: String = '';
  catError: String = '';
  quantityError: String = '';
  purchaseDateError: String = '';
  purchasePriceError: String = '';
  leftSigns: String = '255';

  pItemForm = new FormGroup({
    wkn: new FormControl('', [
      Validators.required,
      Validators.minLength(6)]), // Added Validators.required
    name: new FormControl('',
      Validators.required), // Added Validators.required
    description: new FormControl('', [
      Validators.required,
      Validators.maxLength(255)]), // Added Validators.required
    cat: new FormControl('',
      Validators.required), // Added Validators.required
    quantity: new FormControl('', [
      Validators.required]), // Added Validators.required and a pattern validator for digits
    purchaseDate: new FormControl('', [
      Validators.required, DateValidator()]), // Added Validators.required
    purchasePrice: new FormControl('', [
      Validators.required]) // Added Validators.required and a pattern validator for numbers with up to 2 decimal places
  })

  constructor(private shareService: ShareService) {
  }

  ngOnInit() {
    this.shareService.getShares().subscribe({
      next: (data) => this.shares = data,
      error: (error) => console.error(error),
      complete: () => console.info('complete')
    })
  }

  existingElements(element: ShareModel, propertyName:string, text: string): any {
    let elText = element[propertyName].toLowerCase();
    return(elText.startsWith(text.toLowerCase()));
  }

  onKeyUpWkn(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.sharesFiltered = this.shares.filter(
      share => this.existingElements(share, "wkn", inputElement.value.toLowerCase())
    );
  }

  onKeyUpName(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const nameSelected = document.activeElement === document.querySelector('name')
    this.sharesFiltered = this.shares.filter(
      share => this.existingElements(share, "name", inputElement.value.toLowerCase())
    );
  }

  onBlur() {
    if(this.sharesFiltered.length == 1){
      this.pItemForm.patchValue({
        wkn: this.sharesFiltered[0].wkn,
        name: this.sharesFiltered[0].name,
        description: this.sharesFiltered[0].description,
        cat: this.sharesFiltered[0].category
      })
    }
  }

  onKeyUpDescription(event: Event) {
    console.log('triggered')
    const inputElement = event.target as HTMLInputElement;
    this.leftSigns = String(255 - inputElement.value.length)
  }

  onKeyDownPrice(event: KeyboardEvent) {
    const inputElement = event.target as HTMLInputElement;
    if (event.key == ',' && inputElement.value.length == 0){
      event.preventDefault();
    }
  }

  onSubmit() {
    if(this.pItemForm.invalid){
      if(this.pItemForm.controls.wkn.errors?.['minlength']){
        this.wknError = "Die WKN muss aus 6 Stellen bestehen"
      } else if (this.pItemForm.controls.wkn.errors?.['required']){
        this.wknError = "Bitte füllen sie die WKN aus"
      }
      if(this.pItemForm.controls.name.errors?.['required']){
        this.nameError = "Bitte tragen Sie einen Namen ein"
      }
      if(this.pItemForm.controls.description.errors?.['maxLength']){
        this.descriptionError = "Die Beschreibung darf nicht länger als 255 Zeichen sein"
      } else if (this.pItemForm.controls.description.errors?.['required']){
        this.descriptionError = "Bitte tragen Sie die Beschreibung ein"
      }
      if(this.pItemForm.controls.cat.errors?.['required']){
        this.catError = "Bitte wählen Sie eine Kategorie"
      }
      if(this.pItemForm.controls.quantity.errors?.['required']){
        this.quantityError = "Bitte tragen Sie eine Anzahl ein"
      }
      if(this.pItemForm.controls.purchaseDate.errors?.['required']){
        this.purchaseDateError = "Bitte tragen Sie ein Datum ein"
      } else if(this.pItemForm.controls.purchaseDate.errors?.['dateInvalid']){
        this.purchaseDateError = 'Datum ist ungültig.'
      }
      if (this.pItemForm.controls.purchasePrice.errors?.['required']){
        this.purchasePriceError = "Bitte tragen Sie einen Kaufpreis ein"
      }

    } else {

      console.log(this.pItemForm.value)

      //this.pItemForm.reset();
    }
  }

  clearForm() {
    this.pItemForm.reset();
  }

}