import {ShareModel} from "./share.model";

export class PurchaseModel {
  id?: bigint
  purchasePrice?: number
  purchaseDate?: string
  quantity?: number
  shareDTO?: ShareModel
}
