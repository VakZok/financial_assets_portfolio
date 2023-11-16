import {Component, Input} from '@angular/core';
import {Observable} from "rxjs";
import {PortfolioItemModel} from "../../../../../../core/models/portfolio-item.model";

@Component({
  selector: 'app-meta-data',
  templateUrl: './meta-data.component.html',
  styleUrls: ['./meta-data.component.css']
})
export class MetaDataComponent {
  @Input() data$: Observable<PortfolioItemModel>| null = null;
}
