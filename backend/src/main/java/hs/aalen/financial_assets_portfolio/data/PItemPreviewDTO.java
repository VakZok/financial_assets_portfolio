package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;

public class PItemPreviewDTO {
    /*PROPERTIES OF ENTITY*/
    private Long id;
    private String wkn;
    private String name;
    private double purchasePrice;
    private int quantity;

    public PItemPreviewDTO(Long id, String wkn, String name, double purchasePrice, int quantity) {
        this.id = id;
        this.wkn = wkn;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;

    }
    public PItemPreviewDTO(PortfolioItem portfolioItem){
        this.id = portfolioItem.getId();
        this.wkn = portfolioItem.getShare().getWkn();
        this.name = portfolioItem.getShare().getName();
        this.purchasePrice = portfolioItem.getPurchasePrice();
        this.quantity = portfolioItem.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWkn() {
        return wkn;
    }

    public void setWkn(String wkn) {
        this.wkn = wkn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}