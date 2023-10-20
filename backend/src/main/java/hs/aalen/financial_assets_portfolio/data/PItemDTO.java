package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import java.time.LocalDate;

public class PItemDTO {

    private Long id;
    private LocalDate purchaseDate;
    private double purchasePrice;
    private int quantity;
    private ShareDTO shareDTO;

    public PItemDTO(
            Long id, LocalDate purchaseDate,
            double purchasePrice, int quantity, ShareDTO shareDTO
            ) {

        this.id = id;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.shareDTO = shareDTO;
    }

    public PItemDTO(PortfolioItem portfolioItem) {
        this.id = portfolioItem.getId();
        this.purchaseDate = portfolioItem.getPurchaseDate();
        this.purchasePrice = portfolioItem.getPurchasePrice();
        this.quantity = portfolioItem.getQuantity();
        this.shareDTO = new ShareDTO(portfolioItem.getShare());
    }

    public PItemDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ShareDTO getShareDTO() {
        return shareDTO;
    }

    public void setShareDTO(ShareDTO shareDTO) {
        this.shareDTO = shareDTO;
    }
}
