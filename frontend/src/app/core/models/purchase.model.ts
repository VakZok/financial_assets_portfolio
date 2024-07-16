import {ShareModel} from "./share.model";

// Each PurchaseModel represents a single purchase of a share.

export class PurchaseModel {
  id?: bigint
  purchasePrice?: number
  purchaseDate?: string
  quantity?: number
  shareDTO?: ShareModel
}
