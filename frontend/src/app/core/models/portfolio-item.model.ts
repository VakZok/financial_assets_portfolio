export class PortfolioItemModel {
  id?: bigint
  purchasePrice?: number
  purchaseDate?: Date
  quantity?: number
  wkn: string = '';
  name: string = '';
  category: string = '';
  description: string = '';
}
