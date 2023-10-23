import {ShareModel} from "./share.model";

export class PortfolioItemModel {
  id?: bigint
  purchasePrice?: number
  purchaseDate?: Date
  quantity?: number
  shareDTO?: ShareModel
}
