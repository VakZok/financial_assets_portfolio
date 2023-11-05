import {ShareModel} from "./share.model";

export class PortfolioItemModel {
  id?: bigint
  purchasePrice?: number
  purchaseDate?: string
  quantity?: number
  shareDTO?: ShareModel
}
