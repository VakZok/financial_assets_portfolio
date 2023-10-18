package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import java.time.LocalDate;

public class PItemDTO {

    private Long id;
    private String wkn;
    private String name;
    private String category;
    private String description;
    private LocalDate purchaseDate;
    private double purchasePrice;
    private int quantity;

    public PItemDTO(
            Long id, LocalDate purchaseDate,
            double purchasePrice, int quantity,
            String wkn, String name,
            String category, String description) {

        this.id = id;
        this.wkn = wkn;
        this.name = name;
        this.category = category;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
    }

    public PItemDTO(PortfolioItem portfolioItem) {
        this.id = portfolioItem.getId();
        this.wkn = portfolioItem.getShare().getWkn();
        this.name = portfolioItem.getShare().getName();
        this.category = portfolioItem.getShare().getCategory();
        this.description = portfolioItem.getShare().getDescription();
        this.purchaseDate = portfolioItem.getPurchaseDate();
        this.purchasePrice = portfolioItem.getPurchasePrice();
        this.quantity = portfolioItem.getQuantity();
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
