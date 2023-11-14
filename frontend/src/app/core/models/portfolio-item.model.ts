import {PurchaseModel} from "./purchase.model";
import {ShareModel} from "./share.model";

export class PortfolioItemModel {
  shareDTO?:ShareModel
  avgPrice?: number
  totalQuantity?: number
  purchaseDTOList?: PurchaseModel[]
}
