package hs.aalen.financial_assets_portfolio.data;

import hs.aalen.financial_assets_portfolio.domain.Share;

public class ShareSwaggerDTO {
    private String isin;
    private String name;
    private String type;
    private String description;
    private double price;

    /* Constructors */

    public ShareSwaggerDTO(){}

    /* Getters and Setters */
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }
}
