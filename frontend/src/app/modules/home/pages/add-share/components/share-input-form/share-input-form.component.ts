import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ShareModel} from "../../../../../../core/models/share.model"
import {ShareService} from "../../../../../../core/services/share.service";

const maxSigns : number = 255;

@Component({
  selector: 'share-input-form',
  templateUrl: './share-input-form.component.html',
  styleUrls: ['./share-input-form.component.css']
})

export class ShareInputFormComponent implements OnInit {

  shares: ShareModel[] = [];
  sharesFiltered: ShareModel[] = [];

  errorMap = new Map<string, string>([
    ["wkn", ""],
    ["name", ""],
    ["description", ""],
    ["cat", ""],
  ]);

  leftSigns: string = '255';
  shareAdded: boolean = false;

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

  ngOnInit() {
    this.shareService.getShares().subscribe({
      next: (data) => this.shares = data,
      error: (error) => console.error(error),
      complete: () => console.info('complete')
    })
  }

  // function for autocomplete
  shareExists(element: ShareModel, propertyName:string, text: string): any {
    let elText = (element as any)[propertyName]?.toLowerCase();
    return(elText?.startsWith(text.toLowerCase()));
  }

  //function that searches for matches
  onKeyUpAuto(event: Event, propertyName:string) {
    const inputElement = event.target as HTMLInputElement;
    this.sharesFiltered = this.shares.filter(
      share => this.shareExists(share, propertyName, inputElement.value.toLowerCase())
    );
  }

  //autocompletion for share
  autoFill(event: Event, propertyName:string) {
    const inputElement = event.target as HTMLInputElement;
    this.sharesFiltered = this.shares.filter(
      share => this.shareExists(share, propertyName, inputElement.value.toLowerCase())
    );
    console.log(this.sharesFiltered)
    if(this.sharesFiltered.length == 1){
      this.shareForm.patchValue({
        wkn: this.sharesFiltered[0].wkn,
        name: this.sharesFiltered[0].name,
        description: this.sharesFiltered[0].description,
        cat: this.sharesFiltered[0].category
      })
    }
  }

  // function that counts the amount of left signs
  onKeyUpDescription(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.leftSigns = String(maxSigns - inputElement.value.length)
  }

  onSubmit() {
    //initialize
    this.shareAdded = false;
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

      //send shareDTO to backend. If exception is risen in backend, populate error messages to errorMap
      //successful set "shareAdded" = true and show the success message
      this.shareService.postShare(shareDTO).subscribe({
        next: (data) => {
          console.log(data)
          this.shareForm.reset();
        },
        error: (errors) => errors.error.forEach((item:any) => {
          this.errorMap.set(item.name, item.message);
        }),
        complete: () => this.shareAdded = true
      })
    }
  }

  // method to clear the input form
  clearForm() {
    this.shareForm.reset();
  }

}
