import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ShareService} from "../../../../../../core/services/share.service";

@Component({
  selector: 'share-input-form',
  templateUrl: './share-input-form.component.html',
  styleUrls: ['./share-input-form.component.css']
})

export class ShareInputFormComponent {
  wknError: String = '';
  nameError: String = '';
  descriptionError: String = '';
  catError: String = '';
  leftSigns: String = '255';

  shareForm = new FormGroup({
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
  })

  constructor(private shareService: ShareService) {
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
    if(this.shareForm.invalid){
      if(this.shareForm.controls.wkn.errors?.['minlength']){
        this.wknError = "Die WKN muss aus 6 Stellen bestehen"
      } else if (this.shareForm.controls.wkn.errors?.['required']){
        this.wknError = "Bitte füllen sie die WKN aus"
      }
      if(this.shareForm.controls.name.errors?.['required']){
        this.nameError = "Bitte tragen Sie einen Namen ein"
      }
      if(this.shareForm.controls.description.errors?.['maxLength']){
        this.descriptionError = "Die Beschreibung darf nicht länger als 255 Zeichen sein"
      } else if (this.shareForm.controls.description.errors?.['required']){
        this.descriptionError = "Bitte tragen Sie die Beschreibung ein"
      }
      if(this.shareForm.controls.cat.errors?.['required']){
        this.catError = "Bitte wählen Sie eine Kategorie"
      }

    } else {
      console.log(this.shareForm.value)
      //this.pItemForm.reset();
    }

  }

  clearForm() {
    this.shareForm.reset();
  }

}
