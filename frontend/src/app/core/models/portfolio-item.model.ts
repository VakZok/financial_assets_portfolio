import {ShareModel} from "./share.model";

interface PortfolioItemModel {
  id: bigint
  purchasePrice: number
  purchaseDate: Date
  quantity: number
  share: ShareModel
}
