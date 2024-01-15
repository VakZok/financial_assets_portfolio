package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.ArrayList;


@JsonFilter("pItemFilter")
public class PortfolioItemDTO {
    /** This is the Data Transfer Object (DTO) for the major business case.
     * The transfer object PortfolioItemDTO aggregates information
     * from the database of the backend and is used communicate between
     * frontend and backend. Presents a single PortfolioItem.
     * shareDTO:              A DTO of a share object, associated with the PortfolioItem.
     * purchaseDTOList:       The list of purchases conducted.
     * avgPrice:              The average purchase price per stock over all purchases.
     * totalPrice:            The sum of purchase prices over all purchases.
     * totalQuantity:         The total quantity of bought stocks.
     * currentPurchasePrice:  The current Purchase price for a single stock.
     * isFavorite:            Indicates whether a user likes the PortfolioItem.
     * profitAndLoss:         The profit and loss per stock.
     * profitAndLossCum:      The total profit and loss for the owned stocks.
     *
     */


    @JsonFilter("shareFilter")
    private ShareDTO shareDTO;
    private double avgPrice;
    private double totalPrice;
    private int totalQuantity;
    private double currentPurchasePrice;
    private boolean isFavorite;
    private double profitAndLoss;
    private double profitAndLossCum;
    private ArrayList<PurchaseDTO> purchaseDTOList;


    /* CONSTRUCTORS*/

    public PortfolioItemDTO(
            ShareDTO shareDTO, double avgPrice,
            double totalPrice, int totalQuantity,
            ArrayList<PurchaseDTO> purchaseDTOList,
            double profitAndLoss, double profitAndLossCum,
            boolean isFavorite) {
        this.shareDTO = shareDTO;
        this.avgPrice = avgPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.purchaseDTOList = purchaseDTOList;
        this.profitAndLoss = profitAndLoss;
        this.profitAndLossCum = profitAndLossCum;
        this.isFavorite = isFavorite;
    }

    public PortfolioItemDTO(
            ShareDTO shareDTO, double avgPrice,
            double totalPrice, int totalQuantity,
            ArrayList<PurchaseDTO> purchaseDTOList,
            boolean isFavorite) {
        this.shareDTO = shareDTO;
        this.avgPrice = avgPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.purchaseDTOList = purchaseDTOList;
        this.isFavorite = isFavorite;
    }

    public PortfolioItemDTO(ShareSwaggerDTO shareSwaggerDTO){
        this.shareDTO = new ShareDTO(shareSwaggerDTO);
        this.currentPurchasePrice = shareSwaggerDTO.getPrice();
    }

    public PortfolioItemDTO(){}

    public ShareDTO getShareDTO() {
        return shareDTO;
    }

    public void setShareDTO(ShareDTO shareDTO) {
        this.shareDTO = shareDTO;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public ArrayList<PurchaseDTO> getPurchaseDTOList() {
        return purchaseDTOList;
    }

    public void setPurchaseList(ArrayList<PurchaseDTO> purchaseDTOList) {
        this.purchaseDTOList = purchaseDTOList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public double getProfitAndLossCum() {
        return profitAndLossCum;
    }

    public void setProfitAndLossCum(double profitAndLossCum) {
        this.profitAndLossCum = profitAndLossCum;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public double getCurrentPurchasePrice() {
        return currentPurchasePrice;
    }

    public void setCurrentPurchasePrice(double currentPurchasePrice) {
        this.currentPurchasePrice = currentPurchasePrice;
    }
}
