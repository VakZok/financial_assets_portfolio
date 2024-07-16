import { Component, OnInit} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { PortfolioItemService } from 'app/core/services/portfolio-item.service';
import {PortfolioItemModel} from "../../../../core/models/portfolio-item.model";


@Component({
  selector: 'app-mein-portfolio',
  templateUrl: './my-portfolio.component.html',
  styleUrls: ['./my-portfolio.component.css']
})

export class MyPortfolioComponent {

}

