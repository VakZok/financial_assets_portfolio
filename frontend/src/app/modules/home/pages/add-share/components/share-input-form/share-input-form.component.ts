import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ShareModel} from "../../../../../../core/models/share.model"
import {ShareService} from "../../../../../../core/services/share.service";

const maxSigns : number = 255;

@Component({
  selector: 'share-input-form',
  templateUrl: './share-input-form.component.html',
  styleUrls: ['./share-input-form.component.css']
})

export class ShareInputFormComponent {

  leftSigns: string = '255';
  shareAdded: boolean = false;
  shareChanged: boolean = false;

  errorMap = new Map<string, string>([
    ["wkn", ""],
    ["name", ""],
    ["description", ""],
    ["cat", ""],
  ]);

  /* Form Validation, check for completeness and sanity */
  shareForm = new FormGroup({
    wkn: new FormControl('', [
      Validators.required,
      Validators.minLength(6)]),
    name: new FormControl('',
      Validators.required),
    description: new FormControl('', [
      Validators.required,
      Validators.maxLength(255)]),
    cat: new FormControl('',
      Validators.required)
  })

  constructor(private shareService: ShareService) {
  }

  // function that counts the amount of left signs
  onKeyUpDescription(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.leftSigns = String(maxSigns - inputElement.value.length)
  }

  onSubmit() {
    //initialize
    this.shareAdded = false;
    this.shareChanged = false;
    // loop over input form and remove leading/trailing whitespaces
    for (const controlName in this.shareForm.controls) {
      if (this.shareForm.controls.hasOwnProperty(controlName)) {
        const control = this.shareForm.get(controlName);
        if(control?.value != null){
          control?.setValue(control?.value.trim())
        }
      }
    }
    //check input form. If an input field is invalid, set error text
    if(this.shareForm.invalid){
      if(this.shareForm.controls.wkn.errors?.['minlength']){
        this.errorMap.set("wkn", "Die WKN muss aus 6 Stellen bestehen");
      } else if (this.shareForm.controls.wkn.errors?.['required']){
        this.errorMap.set("wkn","Bitte füllen sie die WKN aus");
      } else {
        this.errorMap.set("wkn", "");
      }

      if(this.shareForm.controls.name.errors?.['required']){
        this.errorMap.set("name", "Bitte tragen Sie einen Namen ein");
      } else {
        this.errorMap.set("name", "");
      }

      if(this.shareForm.controls.description.errors?.['maxLength']){
        this.errorMap.set("description", "Die Beschreibung darf nicht länger als 255 Zeichen sein");
      } else if (this.shareForm.controls.description.errors?.['required']){
        this.errorMap.set("description", "Bitte tragen Sie die Beschreibung ein");
      } else {
        this.errorMap.set("description", "");
      }

      if(this.shareForm.controls.cat.errors?.['required']){
        this.errorMap.set("cat", "Bitte wählen Sie eine Kategorie");
      } else {
        this.errorMap.set("cat", "");
      }

    } else {
      // initialize errors if form is valid
      for (let [key, error] of this.errorMap){
        this.errorMap.set(key, '');
      }
      // create shareDTO
      const shareDTO: ShareModel = {
        wkn: this.shareForm.controls.wkn.value || '',
        name: this.shareForm.controls.name.value || '',
        category: this.shareForm.controls.cat.value || '',
        description: this.shareForm.controls.description.value || ''
      }
      //check if share exists, if exists, sends a put request, if not, sends a post request to add/modify share
      this.shareService.getShare(shareDTO.wkn).subscribe({
        next: (data) => { //if share does not exist in database, an empty shareDTO is returned
          if (data.wkn != null && data.name != null && data.category != null && data.description != null) {
            // if returned shareDTO is not empty, send a put request to modify attributes of existing share in database
            this.shareService.putShare(shareDTO.wkn, shareDTO).subscribe({
              next:(data: ShareModel) => {
                this.shareForm.reset();
              },
              error: (errors) => errors.error.forEach((item:any) => {
                this.errorMap.set(item.name, item.message);
              }),
              complete: () => {
                this.shareChanged = true;
                this.shareAdded = false;
              }
            })
          } else {
            this.shareService.postShare(shareDTO).subscribe({
              next:() => {
                this.shareForm.reset();
              },
              error: (errors) => errors.error.forEach((item:any) => {
                this.errorMap.set(item.name, item.message);
              }),
              complete: () => {
                this.shareAdded = true;
                this.shareChanged = false;
              }
            })
          }
        },
        error: err => {
          console.log(err)
        },
      })
    }
  }

  // method to clear the input form
  clearForm() {
    this.shareForm.reset();
    this.shareChanged = false;
    this.shareAdded = false;
    for (let [key, error] of this.errorMap){
      this.errorMap.set(key, '');
    }
  }

}
