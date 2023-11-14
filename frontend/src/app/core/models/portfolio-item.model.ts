import {ShareModel} from "./share.model";
import {PurchaseModel} from "./purchase.model";

export class PortfolioItemModel {
  shareDTO?: ShareModel
  avgPrice?: number
  totalQuantity?: number
  purchaseDTOList?: PurchaseModel[]
}
