import {ShareModel} from "./share.model";
import {PortfolioItemModel} from "./portfolio-item.model";

export class AggPortfolioItemModel {
  shareDTO?: ShareModel
  avgPrice?: number
  totalQuantity?: number
  pItemDTOList?: PortfolioItemModel[]
}
