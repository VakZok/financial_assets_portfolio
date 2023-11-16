package hs.aalen.financial_assets_portfolio.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("pItemFilter")
public class PortfolioItemDTO {
    @JsonFilter("shareFilter")
    private ShareDTO shareDTO;
    private double avgPrice;
    private double totalPrice;
    private int totalQuantity;
    private ArrayList<PurchaseDTO> purchaseDTOList;

    public PortfolioItemDTO(ShareDTO shareDTO, double avgPrice, double totalPrice, int totalQuantity, ArrayList<PurchaseDTO> purchaseDTOList) {
        this.shareDTO = shareDTO;
        this.avgPrice = avgPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.purchaseDTOList = purchaseDTOList;
    }

    public PortfolioItemDTO(){};

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
}
