import {PurchaseModel} from "./purchase.model";
import {ShareModel} from "./share.model";

/*
A portfolio item is a combination of one share and all its purchases.
For a visual representation, please view the Readme.md file.
 */
export class PortfolioItemModel {
  shareDTO?:ShareModel
  avgPrice?: number
  totalQuantity?: number
  totalPrice?: number
  currentPurchasePrice?: number
  isFavorite?: boolean
  profitAndLoss?: number
  profitAndLossCum?: number
  purchaseDTOList?: PurchaseModel[]
}
